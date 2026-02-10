package com.subscription.plan.utils;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntityDatetime {

    @Column(name = "created_datetime", updatable = false)
    protected LocalDateTime createdDatetime;

    @Column(name = "updated_datetime")
    protected LocalDateTime updatedDatetime;

    @PrePersist
    protected void onCreate() {
        this.createdDatetime = LocalDateTime.now();
        this.updatedDatetime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDatetime = LocalDateTime.now();
    }
}
