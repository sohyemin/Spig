package com.spig.spig.domain.room.signaling.dto;

import lombok.Data;

@Data
public class SignalingMessage {
    private MessageType type;
    private String roomId;
    private String content;
    private String senderId;
    private String targetId;
    Object data;
}
