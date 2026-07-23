package com.spig.spig.domain.room.signaling.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spig.spig.domain.room.signaling.dto.SignalingMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    // WebSocket Session들이 관리하는 리스트
    private final ConcurrentHashMap<String, WebSocketSession> sessions
            = new ConcurrentHashMap<>();
    // room 관리를 위한 리스트 추가
    private final Map<String, Set<WebSocketSession>> roomSessions
            = new ConcurrentHashMap<>();


    /*
    *  [연결 성공] WebSocket 협상 성공 후, WebSocket 연결이 열려 사용할 준비가 된 후에 열림
    *               성공하였을 경우 Session 추가
    * */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.info("New WebSocket connection established: {}", session.getId());
    }

    /*
     *  [메세지 전달] 새로운 WebSocket 메시지가 도착했을 경우 호출
     *               전달 받은 메시지를 순회하며 메시지 전달
     *               Message.getPayload()를 통해 메시지가 전달
     * */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // 클라이언트에게 메시지 받기
            log.info("Receive message from client {} : {}", session.getId(), message.getPayload());

            SignalingMessage signalingMessage = objectMapper.readValue(message.getPayload(), SignalingMessage.class);
            String roomId = signalingMessage.getRoomId();

            Set<WebSocketSession> participants = roomSessions.computeIfAbsent(
                    roomId,
                    key -> ConcurrentHashMap.newKeySet()
            );

            log.info("Processing {} message for room : {}", signalingMessage.getType(), roomId);

            switch (signalingMessage.getType()) {
                case JOIN :
                    handleJoinMessage(session, signalingMessage);
                    break;
                case OFFER :
                    log.info("Receive offer from : {}", session.getId());
                    sendToTarget(session, signalingMessage);
                    break;
                case ANSWER :
                    log.info("Receive answer from : {}", session.getId());
                    sendToTarget(session, signalingMessage);
                    break;
                case ICE_CANDIDATE:
                    log.info("Receive ICE CANDIDATE from : {}", session.getId());
                    sendToTarget(session, signalingMessage);
                    break;
                default:
                    log.warn("Unknown Message type : {}", signalingMessage.getType());
            }
        } catch (Exception e){
            log.error("Error handling Message: ",e);
        }
    }

    private void sendToTarget(WebSocketSession session, SignalingMessage signalingMessage) {

    }

    /*
    * MessageType이 join이었을 경우 실행
    * 방이 없으면 생성하고,
    * 방이 있을 경우 join 시킨다.
    * */
    private void handleJoinMessage(WebSocketSession session, SignalingMessage signalingMessage) {
        if (signalingMessage.getRoomId().isEmpty()){

        }
    }

    /*
     *  [소켓 종료 및 전송 오류] WebSocket 연결이 어느 쪽에서든 종료되거나 전송 오류가 발생한 후 호출
     *               종료 및 실패하였을 때 세션 종료
     * */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        System.out.println("[+] afterConnectionClosed - Session: " + session.getId() + ", CloseStatus: " + status);
    }
}
