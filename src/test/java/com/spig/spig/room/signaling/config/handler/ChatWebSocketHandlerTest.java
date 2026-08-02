package com.spig.spig.room.signaling.config.handler;

import com.spig.spig.domain.room.service.RoomService;
import com.spig.spig.domain.room.signaling.config.handler.ChatWebSocketHandler;
import com.spig.spig.domain.room.signaling.config.handler.SessionRegistry;
import com.spig.spig.domain.room.signaling.config.handler.WebSocketMessageSender;
import com.spig.spig.domain.room.signaling.dto.JoinResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatWebSocketHandlerTest {

    @Mock private RoomService roomService;
    @Mock private SessionRegistry sessionRegistry;
    @Mock private WebSocketMessageSender messageSender;
    @Mock private WebSocketSession session;
    @Mock private WebSocketSession targetSession;

    private ChatWebSocketHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ChatWebSocketHandler(roomService, sessionRegistry, messageSender);
        when(session.getId()).thenReturn("caller");
    }

    @Test
    void connectionEstablished_registersSession() {
        handler.afterConnectionEstablished(session);

        verify(messageSender).register(session);
    }

    @Test
    void secondJoin_sendsJoinSuccessToSelf_andReadyToCaller() throws Exception {
        when(roomService.join(session, "room-1"))
                .thenReturn(JoinResult.callee("caller-session"));

        handler.handleTextMessage(session,
                new TextMessage("{\"type\":\"JOIN\",\"roomId\":\"room-1\"}"));

        ArgumentCaptor<TextMessage> joinMessage = ArgumentCaptor.forClass(TextMessage.class);
        verify(messageSender).send(eq("caller"), joinMessage.capture());
        assertThat(joinMessage.getValue().getPayload())
                .contains("JOIN_SUCCESS", "room-1", "CALLEE");

        ArgumentCaptor<TextMessage> readyMessage = ArgumentCaptor.forClass(TextMessage.class);
        verify(messageSender).send(eq("caller-session"), readyMessage.capture());
        assertThat(readyMessage.getValue().getPayload()).contains("READY");
    }

    @Test
    void offer_isForwardedToOtherParticipant() throws Exception {
        when(targetSession.getId()).thenReturn("callee");
        when(targetSession.isOpen()).thenReturn(true);
        when(sessionRegistry.findTargetSession(eq(session), org.mockito.ArgumentMatchers.any()))
                .thenReturn(targetSession);

        handler.handleTextMessage(session, new TextMessage("""
                {"type":"OFFER","roomId":"room-1","content":"sdp-offer"}
                """));

        ArgumentCaptor<TextMessage> message = ArgumentCaptor.forClass(TextMessage.class);
        verify(messageSender).send(eq("callee"), message.capture());
        assertThat(message.getValue().getPayload()).contains("OFFER", "sdp-offer");
    }

    @Test
    void closedConnection_unregistersAndLeavesRoom() throws Exception {
        handler.afterConnectionClosed(session, CloseStatus.NORMAL);

        verify(messageSender).unregister("caller");
        verify(roomService).leaveRoom(session);
    }
}
