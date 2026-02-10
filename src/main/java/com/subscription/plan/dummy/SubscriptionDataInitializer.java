package com.subscription.plan.dummy;

import com.subscription.plan.common.PlanType;
import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.domain.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionDataInitializer implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM plan", Integer.class) == 0) {
            insertInitialPlans();
        }
        if (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM member", Integer.class) == 0) {
            generateDummyData();
        }
    }

    private void insertInitialPlans() {
        String sql = "INSERT INTO plan (plan_type, duration_months, origin_price, discount_rate, total_price) VALUES (?, ?, ?, ?, ?)";
        for (PlanType type : PlanType.values()) {
            for (int m : List.of(1, 6, 12)) {
                Plan p = Plan.builder().planType(type).durationMonths(m).build();
                jdbcTemplate.update(sql, p.getPlanType().name(), p.getDurationMonths(), p.getOriginPrice(), p.getDiscountRate(), p.getTotalPrice());
            }
        }
    }

    public void generateDummyData() {
        Random random = new Random();

        Map<Long, Integer> planDurationMap = jdbcTemplate.query(
                "SELECT id, duration_months FROM plan",
                (rs, rowNum) -> Map.entry(rs.getLong("id"), rs.getInt("duration_months"))
        ).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<Long> planIds = new ArrayList<>(planDurationMap.keySet());

        List<Object[]> memberBatch = new ArrayList<>();
        List<List<Object[]>> subscriptionBuffer = new ArrayList<>();
        String encodedPassword = passwordEncoder.encode("password");

        for (int i = 1; i <= 500; i++) {
            List<Object[]> userSubscriptions = new ArrayList<>();
            double d = random.nextDouble();

            if (d < 0.1) addWithPending(userSubscriptions, 0, planIds);
            else if (d < 0.7) addSub(userSubscriptions, 0, planIds, planDurationMap, SubscriptionStatus.ACTIVE, random, false);
            else if (d < 0.85) addSub(userSubscriptions, 0, planIds, planDurationMap, SubscriptionStatus.CANCELED, random, false);
            else {
                addSub(userSubscriptions, 0, planIds, planDurationMap, SubscriptionStatus.EXPIRED, random, true);
                addSub(userSubscriptions, 0, planIds, planDurationMap, SubscriptionStatus.ACTIVE, random, false);
            }

            LocalDateTime earliestSubStart = userSubscriptions.stream()
                    .map(row -> (LocalDateTime) row[5])
                    .min(LocalDateTime::compareTo)
                    .orElse(LocalDateTime.now());

            LocalDateTime memberCreatedAt = earliestSubStart.minusDays(random.nextInt(7) + 1);

            memberBatch.add(new Object[]{
                    "User_" + String.format("%03d", i),
                    encodedPassword,
                    memberCreatedAt,
                    memberCreatedAt
            });
            subscriptionBuffer.add(userSubscriptions);
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO member (user_name, password, created_datetime, updated_datetime) VALUES (?, ?, ?, ?)",
                memberBatch
        );

        List<Long> memberIds = jdbcTemplate.queryForList("SELECT id FROM member ORDER BY id ASC", Long.class);

        List<Object[]> finalSubscriptionBatch = new ArrayList<>();
        for (int i = 0; i < memberIds.size(); i++) {
            Long realMemberId = memberIds.get(i);
            List<Object[]> userSubs = subscriptionBuffer.get(i);
            for (Object[] subRow : userSubs) {
                subRow[0] = realMemberId;
                finalSubscriptionBatch.add(subRow);
            }
        }

        String subSql = "INSERT INTO subscription (member_id, plan_id, status, cycle, auto_renewal, start_date, end_date, cancel_date, pending_plan_id, change_effective_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(subSql, finalSubscriptionBatch);
    }

    private void addWithPending(List<Object[]> list, long mId, List<Long> pIds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusDays(15);
        // [member_id, plan_id, status, cycle, auto_renewal, start_date, end_date, cancel_date, pending_plan_id, change_effective_date]
        list.add(new Object[]{mId, pIds.get(0), "ACTIVE", 5, true, now.minusDays(15), end, null, pIds.get(pIds.size() - 1), end});
    }

    private void addSub(List<Object[]> list, long mId, List<Long> pIds, Map<Long, Integer> planDurationMap, SubscriptionStatus s, Random r, boolean past) {
        Long pId = pIds.get(r.nextInt(pIds.size()));
        int dur = planDurationMap.get(pId);

        LocalDateTime now = LocalDateTime.now();
        int c = (s == SubscriptionStatus.ACTIVE && !past) ? r.nextInt(5) + 1 : 1;

        // 과거 데이터일 경우 약 1~2년 전으로 설정
        LocalDateTime start = past ?
                now.minusMonths(12 + r.nextInt(12)) :
                now.minusMonths((long) c * dur).plusDays(r.nextInt(10));

        LocalDateTime end = start.plusMonths(dur);

        list.add(new Object[]{
                mId, pId, s.name(), c,
                s == SubscriptionStatus.ACTIVE,
                start,
                end,
                s == SubscriptionStatus.CANCELED ? now.minusDays(2) : null,
                null,
                null
        });
    }
}