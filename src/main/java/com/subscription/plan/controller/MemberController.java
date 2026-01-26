package com.subscription.plan.controller;

import com.subscription.plan.domain.Member;
import com.subscription.plan.dto.MemberSignUpRequestDto;
import com.subscription.plan.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping()
    public ResponseEntity<Member> signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        Member member = memberService.saveMember(memberSignUpRequestDto);
        return ResponseEntity.ok(member);
    }
}
