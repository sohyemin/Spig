package com.spig.spig.domain.room.service;

import com.spig.spig.domain.room.model.Room;
import com.spig.spig.domain.room.signaling.dto.JoinResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService{

    private final int MAX_SIZE_PARTICIPANTS = 2;

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public JoinResult join(WebSocketSession session, String roomId) {

        session.getAttributes().put("roomId", roomId);

        Room room = rooms.computeIfAbsent(
                roomId,
                Room::new
        );

        return room.join(session);
    }

    @Override
    public void search() {

    }

    @Override
    public void leaveRoom(WebSocketSession session) {
        Object roomIdAttribute =
                session.getAttributes().get("roomId");

        if (roomIdAttribute == null) {
            log.debug(
                    "session {}은 참여한 방이 없어 leaveRoom을 생략합니다.",
                    session.getId()
            );
            return;
        }

        String roomId = roomIdAttribute.toString();

        Room room = rooms.get(roomId);

        if (room == null) {
            log.warn(
                    "room {}을 찾을 수 없습니다. sessionId={}",
                    roomId,
                    session.getId()
            );
            return;
        }

        room.leave(session);

        if(room.isEmpty()){
            rooms.remove(roomId, room);
        }

        session.getAttributes().remove("roomId");
    }

    @Override
    public WebSocketSession findOtherParticipant(WebSocketSession session, String roomId) {
        Set<WebSocketSession> participants = rooms.get(roomId).getParticipants();

        if (!(participants.size()==2)){
            return null;
        }

        for (WebSocketSession participant: participants){
            if(!participant.getId().equals(session.getId())){
                return participant;
            }
        }

        return null;
    }
}
