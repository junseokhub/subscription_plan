package com.subscription.plan.dto;

import com.subscription.plan.domain.Member;

public record MemberResponseDto(Long id, String userName) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getId(), member.getUserName());
    }
}