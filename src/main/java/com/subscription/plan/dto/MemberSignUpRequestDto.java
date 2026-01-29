package com.subscription.plan.dto;

import lombok.Getter;

@Getter
public class MemberSignUpRequestDto {
    private String userName;

    public MemberSignUpRequestDto(String userName) {
        this.userName = userName;
    }
}
