package com.spig.spig.domain.room.model;

import com.spig.spig.domain.room.signaling.dto.JoinResult;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Slf4j
public class Room {

    private final String roomId;
    private String caller;
    private final Set<WebSocketSession> participants;

    public Room(String roomId) {
        this.roomId = roomId;
        this.participants = ConcurrentHashMap.newKeySet();
    }

    public synchronized JoinResult join(WebSocketSession session) {
        if (participants.contains(session)){
            log.info("room {}에 이미 session {}이 존재합니다.", roomId, session.getId());
            return JoinResult.failed();
        }

        if(participants.size() >= 2){
            log.warn("room{}의 정원이 가득 찼습니다.", roomId);
            return JoinResult.failed();
        }
        participants.add(session);

        log.info(
                "session {}이 room {}에 입장했습니다. 현재 인원: {}",
                session.getId(),
                roomId,
                participants.size()
        );

        if(participants.size() == 1) {
            this.caller = session.getId();
            log.info("caller : {}", session.getId());
            return JoinResult.caller();
        }


        log.info("caller : {}", caller);
        return JoinResult.callee(caller);
    }

    public synchronized void leave(WebSocketSession session) {
        participants.remove(session);
    }

    public boolean isEmpty() {
        return participants.isEmpty();
    }
}

