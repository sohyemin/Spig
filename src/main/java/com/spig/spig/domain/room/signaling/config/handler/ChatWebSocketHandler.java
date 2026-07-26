package com.spig.spig.domain.room.signaling.config.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spig.spig.domain.room.service.RoomService;
import com.spig.spig.domain.room.signaling.dto.JoinResult;
import com.spig.spig.domain.room.signaling.dto.SignalingMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    // room 관리를 위한 리스트 추가
    private final RoomService roomService;
    // session 관련
    private final SessionRegistry sessionRegistry;
    // sendMessage 관리
    private final WebSocketMessageSender messageSender;


    /*
    *  [연결 성공] WebSocket 협상 성공 후, WebSocket 연결이 열려 사용할 준비가 된 후에 열림
    *               성공하였을 경우 Session 추가
    * */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // sendMessage 동시성 관리를 위해 추가
        messageSender.register(session);
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

            //objectMapper를 통해 읽어온 정보를 signalingMessage에 매핑
            SignalingMessage signalingMessage = objectMapper.readValue(message.getPayload(), SignalingMessage.class);
            String roomId = signalingMessage.getRoomId();

            log.info("Processing {} message for room : {}", signalingMessage.getType(), roomId);

            switch (signalingMessage.getType()) {
                case JOIN -> {
                    handleJoinMessage(session, signalingMessage);
                }
                case OFFER,ANSWER,ICE_CANDIDATE -> {
                    log.info("Receive {} from : {}", signalingMessage.getType(),session.getId());
                    sendToTarget(session, signalingMessage);
                }
                default ->
                    log.warn("Unknown Message type : {}", signalingMessage.getType());
            }
        } catch (Exception e){
            log.error("Error handling Message: ",e);
        }
    }

    private void sendToTarget(WebSocketSession session, SignalingMessage signalingMessage) throws IOException {

        WebSocketSession targetSession = sessionRegistry.findTargetSession(session, signalingMessage);

        if (targetSession == null || !targetSession.isOpen()) {
            log.warn("Target session not found or closed: {}", session.getId());
            return;
        }

        String payload = objectMapper.writeValueAsString(signalingMessage);
        messageSender.send(session.getId(), new TextMessage(payload));
    }

    /*
    * MessageType이 join이었을 경우 실행
    * 방이 없으면 생성하고,
    * 방이 있을 경우 join 시킨다.
    * */
    private void handleJoinMessage(WebSocketSession session, SignalingMessage signalingMessage) throws IOException {
        String roomId = signalingMessage.getRoomId();

        if (roomId == null || roomId.isBlank()) {
            log.warn("방 번호가 입력되지 않았습니다.");
            return;
        }

        JoinResult joined = roomService.join(session, roomId);

        if(!joined.isSuccess()){
            log.warn(
                    "Session {}에서 room {} 접속이 실패했습니다.",
                    session.getId(),
                    roomId
            );
        }

        Map<String, Object> response = new HashMap<>();

        response.put("type", "JOIN_SUCCESS");
        response.put("roomId", roomId);
        response.put("role", joined.getRole());

        messageSender.send(session.getId(), new TextMessage(objectMapper.writeValueAsString(response)));

        if (joined.shouldsendReady()){
            messageSender.send(
                    joined.getReadyTargetSessionId(),
                    new TextMessage("""
                            {
                             "type":"READY"
                            }                            
                            """)
            );
        }

    }

    /*
     *  [소켓 종료 및 전송 오류] WebSocket 연결이 어느 쪽에서든 종료되거나 전송 오류가 발생한 후 호출
     *               종료 및 실패하였을 때 세션 종료
     * */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        messageSender.unregister(session.getId());
        roomService.leaveRoom(session);
        System.out.println("[+] afterConnectionClosed - Session: " + session.getId() + ", CloseStatus: " + status);
    }
}
