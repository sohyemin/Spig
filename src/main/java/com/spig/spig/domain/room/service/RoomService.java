package com.spig.spig.domain.room.service;

import com.spig.spig.domain.room.signaling.dto.JoinResult;
import jakarta.websocket.Session;
import org.springframework.web.socket.WebSocketSession;

public interface RoomService {
    public JoinResult join(WebSocketSession session, String userId);

    public void search();

    public void leaveRoom(WebSocketSession session);

    public WebSocketSession findOtherParticipant(WebSocketSession session, String roomId);
}
