package com.spig.spig.room.model;

import com.spig.spig.domain.room.model.Room;
import com.spig.spig.domain.room.signaling.dto.JoinResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class RoomTest {

    private Room room;
    private WebSocketSession caller;
    private WebSocketSession callee;

    @BeforeEach
    void setUp() {
        room = new Room("room-1");
        caller = session("caller");
        callee = session("callee");
    }

    // CALLER 입장
    @Test
    void firstParticipant_becomesCaller() {
        JoinResult result = room.join(caller);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getRole()).isEqualTo("CALLER");
        assertThat(room.getParticipants()).containsExactly(caller);
    }

    // CALLEE 입장
    // 타겟 콜러 들어갔는지 확인
    @Test
    void secondParticipant_becomesCallee_andTargetsCallerForReady() {
        room.join(caller);

        JoinResult result = room.join(callee);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getRole()).isEqualTo("CALLEE");
        assertThat(result.getReadyTargetSessionId()).isEqualTo("caller");
    }


    // 세번째 참가자 등록 거절 여부 확인
    @Test
    void thirdParticipant_isRejected() {
        WebSocketSession third = session("third");
        room.join(caller);
        room.join(callee);

        JoinResult result = room.join(third);

        assertThat(result.isSuccess()).isFalse();
        assertThat(room.getParticipants()).hasSize(2).doesNotContain(third);
    }


    // 중복 참가자 참여 거절
    @Test
    void duplicateParticipant_isRejected() {
        room.join(caller);

        JoinResult result = room.join(caller);

        assertThat(result.isSuccess()).isFalse();
        assertThat(room.getParticipants()).containsExactly(caller);
    }

    private WebSocketSession session(String id) {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn(id);
        return session;
    }
}
