package com.spig.spig.domain.room.signaling.dto;

import lombok.Getter;

@Getter
public class JoinResult {
    private boolean success;
    private String role;

    public JoinResult(boolean success) {
        this.success = success;
    }

    public JoinResult(boolean success, String role) {
        this.success = success;
        this.role = role;
    }
}
