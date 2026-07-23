package com.spig.spig.domain.room.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Slf4j
public class Room {

    private final String roomId;
    private final Set<WebSocketSession> participants;

    public Room(String roomId) {
        this.roomId = roomId;
        this.participants = ConcurrentHashMap.newKeySet();
    }

    public synchronized boolean join(WebSocketSession session) {
        if (participants.contains(session)){
            log.info("room {}에 이미 session {}이 존재합니다.", roomId, session.getId());
            return true;
        }

        if(participants.size() >= 2){
            log.warn("room{}의 정원이 가득 찼습니다.", roomId);
            return false;
        }
        participants.add(session);

        log.info(
                "session {}이 room {}에 입장했습니다. 현재 인원: {}",
                session.getId(),
                roomId,
                participants.size()
        );

        return true;
    }

    public synchronized void leave(WebSocketSession session) {
        participants.remove(session);
    }

    public boolean isEmpty() {
        return participants.isEmpty();
    }
}

