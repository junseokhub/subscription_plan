package com.subscription.plan.controller;

import com.subscription.plan.domain.Member;
import com.subscription.plan.dto.MemberChangeNameRequestDto;
import com.subscription.plan.dto.MemberResponseDto;
import com.subscription.plan.dto.MemberSignUpRequestDto;
import com.subscription.plan.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/find")
    public MemberResponseDto getMember(@RequestParam String username) {
        return MemberResponseDto.from(memberService.findMember(username));
    }

    @PostMapping()
    public MemberResponseDto signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        Member member = memberService.saveMember(memberSignUpRequestDto);
        return MemberResponseDto.from(member);
    }
    
    @PostMapping("/update")
    public MemberResponseDto changeUserName(@RequestBody MemberChangeNameRequestDto memberChangeNameRequestDto) {
        Member member = memberService.changeUserName(memberChangeNameRequestDto);
        return MemberResponseDto.from(member);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody String userName) {
        memberService.deleteUser(userName);
    }
}
