package com.subscription.plan.service;

import com.subscription.plan.domain.Member;
import com.subscription.plan.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMember(String userName) {
        return memberRepository.findByUserName(userName);
    }
}
