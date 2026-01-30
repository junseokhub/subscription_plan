package com.subscription.plan.repository;

import com.subscription.plan.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Success Find Member")
    void findMember_success() {
        Member member = Member.builder().userName("testUser").build();
        memberRepository.save(member);

        Optional<Member> result = memberRepository.findByUserName("testUser");

        assertThat(result).isPresent();
        assertThat(result.get().getUserName()).isEqualTo("testUser");
    }
}