package com.subscription.plan.domain;

import com.subscription.plan.utils.BaseEntityDatetime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntityDatetime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;

    public void changeUserName(String userName) {
        this.userName = userName;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    @Builder
    public Member(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}

