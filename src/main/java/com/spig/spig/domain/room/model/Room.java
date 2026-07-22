package com.spig.spig.domain.room.model;

import java.util.HashSet;
import java.util.Set;

public class Room {
    private final Set<String> participants = new HashSet<>();

    public void join(String userId) throws IllegalAccessException {
        if(participants.size() >= 2){
            throw new IllegalAccessException("방이 가득 찼습니다.");
        }
        participants.add(userId);
    }

    public void leave(String userId) {
        participants.remove(userId);
    }

    public boolean isEmpty() {
        return participants.isEmpty();
    }
}
