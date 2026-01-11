package com.subscription.plan.repository;

import com.subscription.plan.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserName(String userName);
}
