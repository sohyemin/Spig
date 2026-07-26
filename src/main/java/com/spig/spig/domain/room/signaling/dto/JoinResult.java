package com.spig.spig.domain.room.signaling.dto;

import lombok.Getter;

@Getter
public class JoinResult {
    private boolean success;
    private String role;
    private String readyTargetSessionId;

    public static  JoinResult failed() {
        return new JoinResult(false, null, null);
    }

    public static JoinResult caller(){
        return new JoinResult(true, "CALLER", null);
    }

    public static JoinResult callee(String readyTargetSessionId){
        return new JoinResult(true, "CALLEE", readyTargetSessionId);
    }

    public boolean shouldsendReady(){
        return readyTargetSessionId !=null;
    }

    public JoinResult(boolean success, String role, String readyTargetSessionId) {
        this.success = success;
        this.role = role;
        this.readyTargetSessionId = getReadyTargetSessionId();
    }
}
