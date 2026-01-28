package com.subscription.plan.service;

import com.subscription.plan.domain.Member;
import com.subscription.plan.dto.MemberChangeNameRequestDto;
import com.subscription.plan.dto.MemberResponseDto;
import com.subscription.plan.dto.MemberSignUpRequestDto;
import com.subscription.plan.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponseDto findMember(String userName) {
        return memberRepository.findByUserName(userName)
                .map(MemberResponseDto::from) // Entity -> Record 변환
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public MemberResponseDto saveMember(MemberSignUpRequestDto dto) {
        validateDuplicateMember(dto.getUserName());

        Member member = Member.builder()
                .userName(dto.getUserName())
                .build();

        Member savedMember = memberRepository.save(member);
        return MemberResponseDto.from(savedMember);
    }

    @Transactional
    public MemberResponseDto changeUserName(MemberChangeNameRequestDto dto) {
        Member member = memberRepository.findByUserName(dto.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        member.changeUserName(dto.getNewName());

        return MemberResponseDto.from(member);
    }

    public void deleteUser(String userName) {
        Member member = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        memberRepository.delete(member);
    }

    private void validateDuplicateMember(String userName) {
        memberRepository.findByUserName(userName)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
