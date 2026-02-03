package com.subscription.plan.dummy;

import com.subscription.plan.common.PlanType;
import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.domain.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SubscriptionDataInitializer implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

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
        List<Object[]> members = new ArrayList<>();
        List<Object[]> subs = new ArrayList<>();
        List<Long> planIds = jdbcTemplate.queryForList("SELECT id FROM plan ORDER BY id ASC", Long.class);

        for (int i = 1; i <= 500; i++) {
            members.add(new Object[]{"User_" + String.format("%03d", i)});
            double d = random.nextDouble();
            if (d < 0.1) addWithPending(subs, i, planIds);
            else if (d < 0.7) addSub(subs, i, planIds, SubscriptionStatus.ACTIVE, random, false);
            else if (d < 0.85) addSub(subs, i, planIds, SubscriptionStatus.CANCELED, random, false);
            else {
                addSub(subs, i, planIds, SubscriptionStatus.EXPIRED, random, true);
                addSub(subs, i, planIds, SubscriptionStatus.ACTIVE, random, false);
            }
        }

        jdbcTemplate.batchUpdate("INSERT INTO member (user_name) VALUES (?)", members);
        String sql = "INSERT INTO subscription (member_id, plan_id, status, cycle, auto_renewal, start_date, end_date, cancel_date, pending_plan_id, change_effective_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, subs);
    }

    private void addWithPending(List<Object[]> list, long mId, List<Long> pIds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusDays(15);
        list.add(new Object[]{mId, pIds.get(0), "ACTIVE", 5, true, now.minusDays(15), end, null, pIds.get(8), end});
    }

    private void addSub(List<Object[]> list, long mId, List<Long> pIds, SubscriptionStatus s, Random r, boolean past) {
        Long pId = pIds.get(r.nextInt(pIds.size()));
        int dur = jdbcTemplate.queryForObject("SELECT duration_months FROM plan WHERE id = ?", Integer.class, pId);
        LocalDateTime now = LocalDateTime.now();
        int c = (s == SubscriptionStatus.ACTIVE && !past) ? r.nextInt(5) + 1 : 1;
        LocalDateTime start = past ? now.minusMonths(15) : now.minusMonths((long) c * dur).plusDays(r.nextInt(10));
        LocalDateTime end = start.plusMonths(dur);
        list.add(new Object[]{mId, pId, s.name(), c, s == SubscriptionStatus.ACTIVE, start, end, s == SubscriptionStatus.CANCELED ? now.minusDays(2) : null, null, null});
    }
}