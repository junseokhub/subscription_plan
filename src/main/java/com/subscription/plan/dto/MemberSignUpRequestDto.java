package com.subscription.plan.dto;

import lombok.Getter;

@Getter
public class MemberSignUpRequestDto {
    private String userName;
    private String password;

    public MemberSignUpRequestDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
