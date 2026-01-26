package com.subscription.plan.service;

import com.subscription.plan.domain.Member;
import com.subscription.plan.dto.MemberSignUpRequestDto;
import com.subscription.plan.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private void validateDuplicateMember(String userName) {
        memberRepository.findByUserName(userName)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public Optional<Member> findMember(String userName) {
        return memberRepository.findByUserName(userName);
    }

    @Transactional
    public Member saveMember(MemberSignUpRequestDto dto) {
        validateDuplicateMember(dto.getUserName());

        Member member = Member.builder()
                .userName(dto.getUserName())
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public Member changeUserName(String userName, String newUserName) {
        Member member = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        member.changeUserName(newUserName);

        return member;
    }
}
