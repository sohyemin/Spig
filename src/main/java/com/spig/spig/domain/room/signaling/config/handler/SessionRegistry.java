package com.spig.spig.domain.room.signaling.config.handler;

import com.spig.spig.domain.room.service.RoomService;
import com.spig.spig.domain.room.signaling.dto.SignalingMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
public class SessionRegistry {
    private final RoomService roomService;

    public WebSocketSession findTargetSession(WebSocketSession session, SignalingMessage signalingMessage){
        return roomService.findOtherParticipant(session, signalingMessage.getRoomId());
    }
}
