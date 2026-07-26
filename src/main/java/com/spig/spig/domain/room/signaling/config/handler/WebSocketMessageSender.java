package com.spig.spig.domain.room.signaling.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketMessageSender {

    private final Map<String, WebSocketSession> sessions =
        new ConcurrentHashMap<>();

    public void register(WebSocketSession rawsession){
        WebSocketSession safesession = new ConcurrentWebSocketSessionDecorator(
                rawsession,
                10_000,
                512 * 1024
        );

        sessions.put(rawsession.getId(), safesession);
    }

    public void unregister(String sessionId){
        sessions.remove(sessionId);
    }

    public void send(String sessionId, WebSocketMessage<?> message) throws IOException {
        WebSocketSession session = sessions.get(sessionId);

        if(session==null){
            log.warn("메시지 전송 대상인 세션을 찾을 수 없습니다. sessionId = {}", sessionId);
            return;
        }

        if(!session.isOpen()){
            log.warn("이미 종료된 세션입니다. sessionId = {}", sessionId);
            return;
        }

        session.sendMessage(message);
    }
}
