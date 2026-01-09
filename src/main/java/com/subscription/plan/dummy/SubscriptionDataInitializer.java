package com.subscription.plan.dummy;

import com.subscription.plan.common.PlanType;
import com.subscription.plan.common.SubscriptionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SubscriptionDataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM member", Integer.class);
        if (count == 0) {
            generateDummyData();
            System.out.println(">>> Finished generating dummy data.");
        } else {
            System.out.println(">>> Dummy data already exists.");
        }
    }

    @Transactional
    public void generateDummyData() {
        Random random = new Random();
        List<Object[]> members = new ArrayList<>();
        List<Object[]> subscriptions = new ArrayList<>();

        for (int i = 1; i <= 500; i++) {
            String userName = "User_" + String.format("%03d", i);
            members.add(new Object[]{userName});

            double userCase = random.nextDouble();

            if (userCase < 0.70) {
                // [Case 1] 순수 ACTIVE 유저 (70%)
                addSubscription(subscriptions, i, SubscriptionStatus.ACTIVE, random, false);
            } else if (userCase < 0.80) {
                // [Case 2] 해지 예약 유저 (10%) - 현재 ACTIVE이나 CANCELED 상태
                addSubscription(subscriptions, i, SubscriptionStatus.CANCELED, random, false);
            } else if (userCase < 0.90) {
                // [Case 3] 단순 만료 유저 (10%) - 과거 이력만 존재
                addSubscription(subscriptions, i, SubscriptionStatus.EXPIRED, random, false);
            } else {
                // [Case 4] 재구독 유저 (10%) - EXPIRED 이력 + 현재 ACTIVE
                addSubscription(subscriptions, i, SubscriptionStatus.EXPIRED, random, true); // 과거 이력
                addSubscription(subscriptions, i, SubscriptionStatus.ACTIVE, random, false); // 현재 구독
            }
        }

        // 1. Member 일괄 삽입
        jdbcTemplate.batchUpdate("INSERT INTO member (user_name) VALUES (?)", members);

        // 2. Subscription 일괄 삽입
        String subSql = "INSERT INTO subscription (member_id, plan_type, price, status, cycle, auto_renewal, " +
                "start_date, end_date, cancel_date, pending_plan_type, change_effective_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(subSql, subscriptions);
    }

    private void addSubscription(List<Object[]> list, long memberId, SubscriptionStatus status, Random random, boolean isPastHistory) {
        PlanType plan = PlanType.values()[random.nextInt(PlanType.values().length)];
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start;
        LocalDateTime end;
        int cycle;

        if (isPastHistory) {
            // [과거 기록] 완전히 종료된 이력 (예: 6개월 전 가입해서 5개월 전에 끝남)
            cycle = 1;
            start = now.minusMonths(6).minusDays(random.nextInt(30));
            end = start.plusMonths(1);
        } else {
            // [현재 기록] 진행 중이거나 최근에 끝난 이력
            cycle = random.nextInt(12) + 1;
            start = now.minusMonths(cycle).plusDays(random.nextInt(10));
            end = start.plusMonths(1);
        }

        Long price = plan.getPrice();
        boolean autoRenewal = (status == SubscriptionStatus.ACTIVE);

        // CANCELED는 '해지 예약' 상태이므로 종료일(end)은 미래여야 함
        LocalDateTime cancelDate = (status == SubscriptionStatus.CANCELED) ? now.minusDays(random.nextInt(5)) : null;

        // 플랜 변경 예약 (ACTIVE 유저 중 10%만)
        String pendingPlan = null;
        LocalDateTime effectiveDate = null;
        if (status == SubscriptionStatus.ACTIVE && !isPastHistory && random.nextDouble() < 0.1) {
            pendingPlan = PlanType.PREMIUM.name();
            effectiveDate = end; // 다음 갱신일에 변경
        }

        list.add(new Object[]{
                memberId, plan.name(), price, status.name(), cycle, autoRenewal,
                start, end, cancelDate, pendingPlan, effectiveDate
        });
    }
}