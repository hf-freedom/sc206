package com.meeting.service;

import com.meeting.model.MeetingRoom;
import com.meeting.repository.DataStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingRoomService {

    public List<MeetingRoom> getAllRooms() {
        return new ArrayList<>(DataStore.rooms.values());
    }

    public MeetingRoom getRoomById(Long id) {
        return DataStore.rooms.get(id);
    }

    public MeetingRoom createRoom(MeetingRoom room) {
        room.setId(DataStore.roomIdGenerator.getAndIncrement());
        room.setAvailable(true);
        DataStore.rooms.put(room.getId(), room);
        return room;
    }

    public MeetingRoom updateRoom(Long id, MeetingRoom room) {
        MeetingRoom existingRoom = DataStore.rooms.get(id);
        if (existingRoom == null) {
            return null;
        }
        room.setId(id);
        DataStore.rooms.put(id, room);
        return room;
    }

    public boolean deleteRoom(Long id) {
        return DataStore.rooms.remove(id) != null;
    }
}
