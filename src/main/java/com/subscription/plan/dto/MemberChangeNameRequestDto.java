package com.subscription.plan.dto;

import lombok.Getter;

@Getter
public class MemberChangeNameRequestDto {
    private String userName;
    private String newName;

    public MemberChangeNameRequestDto(String userName, String newName) {
        this.userName = userName;
        this.newName = newName;
    }
}
