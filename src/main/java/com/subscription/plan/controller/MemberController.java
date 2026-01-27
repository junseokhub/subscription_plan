package com.subscription.plan.controller;

import com.subscription.plan.domain.Member;
import com.subscription.plan.dto.MemberChangeNameRequestDto;
import com.subscription.plan.dto.MemberSignUpRequestDto;
import com.subscription.plan.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/find")
    public ResponseEntity<Member> getMember(@RequestBody() String username) {
        return memberService.findMember(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Member> signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        Member member = memberService.saveMember(memberSignUpRequestDto);
        return ResponseEntity.ok(member);
    }
    
    @PostMapping("/update")
    public ResponseEntity<Member> changeUserName(@RequestBody MemberChangeNameRequestDto memberChangeNameRequestDto) {
        Member member = memberService.changeUserName(memberChangeNameRequestDto);
        return ResponseEntity.ok(member);
    }
}
