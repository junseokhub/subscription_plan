package com.subscription.plan.controller;

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
        return memberService.findMember(username);
    }

    @PostMapping()
    public MemberResponseDto signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        return memberService.saveMember(memberSignUpRequestDto);
    }
    
    @PostMapping("/update")
    public MemberResponseDto changeUserName(@RequestBody MemberChangeNameRequestDto memberChangeNameRequestDto) {
        return memberService.changeUserName(memberChangeNameRequestDto);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody String userName) {
        memberService.deleteUser(userName);
    }
}
