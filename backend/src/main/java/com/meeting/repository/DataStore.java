package com.meeting.repository;

import com.meeting.model.Booking;
import com.meeting.model.BookingStatus;
import com.meeting.model.MeetingRoom;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DataStore {
    public static final Map<Long, MeetingRoom> rooms = new ConcurrentHashMap<>();
    public static final Map<Long, Booking> bookings = new ConcurrentHashMap<>();
    public static final AtomicLong roomIdGenerator = new AtomicLong(1);
    public static final AtomicLong bookingIdGenerator = new AtomicLong(1);

    static {
        initRooms();
    }

    private static void initRooms() {
        MeetingRoom room1 = new MeetingRoom();
        room1.setId(roomIdGenerator.getAndIncrement());
        room1.setName("101会议室");
        room1.setCapacity(10);
        room1.setFloor(1);
        room1.setEquipment(Arrays.asList("投影仪", "白板"));
        room1.setOpenTime(LocalTime.of(8, 0));
        room1.setCloseTime(LocalTime.of(20, 0));
        room1.setAvailable(true);
        rooms.put(room1.getId(), room1);

        MeetingRoom room2 = new MeetingRoom();
        room2.setId(roomIdGenerator.getAndIncrement());
        room2.setName("201会议室");
        room2.setCapacity(20);
        room2.setFloor(2);
        room2.setEquipment(Arrays.asList("投影仪", "视频会议", "白板", "电视"));
        room2.setOpenTime(LocalTime.of(8, 0));
        room2.setCloseTime(LocalTime.of(22, 0));
        room2.setAvailable(true);
        rooms.put(room2.getId(), room2);

        MeetingRoom room3 = new MeetingRoom();
        room3.setId(roomIdGenerator.getAndIncrement());
        room3.setName("301会议室");
        room3.setCapacity(50);
        room3.setFloor(3);
        room3.setEquipment(Arrays.asList("投影仪", "视频会议", "音响系统", "白板"));
        room3.setOpenTime(LocalTime.of(8, 0));
        room3.setCloseTime(LocalTime.of(22, 0));
        room3.setAvailable(true);
        rooms.put(room3.getId(), room3);

        MeetingRoom room4 = new MeetingRoom();
        room4.setId(roomIdGenerator.getAndIncrement());
        room4.setName("102小会议室");
        room4.setCapacity(4);
        room4.setFloor(1);
        room4.setEquipment(Arrays.asList("白板"));
        room4.setOpenTime(LocalTime.of(9, 0));
        room4.setCloseTime(LocalTime.of(18, 0));
        room4.setAvailable(true);
        rooms.put(room4.getId(), room4);
    }

    public static List<Booking> getBookingsByRoomAndDate(Long roomId, LocalDate date) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getRoomId().equals(roomId) && booking.getDate().equals(date) &&
                (booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.PENDING_APPROVAL)) {
                result.add(booking);
            }
        }
        return result;
    }

    public static List<Booking> getPendingApprovalBookings() {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getStatus() == BookingStatus.PENDING_APPROVAL) {
                result.add(booking);
            }
        }
        return result;
    }

    public static List<Booking> getBookingsByStatus(BookingStatus status) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getStatus() == status) {
                result.add(booking);
            }
        }
        return result;
    }
}
