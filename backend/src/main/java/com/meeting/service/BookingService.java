package com.meeting.service;

import com.meeting.model.Booking;
import com.meeting.model.BookingStatus;
import com.meeting.model.MeetingRoom;
import com.meeting.repository.DataStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    public List<Booking> getAllBookings() {
        return new ArrayList<>(DataStore.bookings.values());
    }

    public Booking getBookingById(Long id) {
        return DataStore.bookings.get(id);
    }

    public List<Booking> getBookingsByRoomAndDate(Long roomId, LocalDate date) {
        return DataStore.getBookingsByRoomAndDate(roomId, date);
    }

    public Booking createBooking(Booking booking) {
        MeetingRoom room = DataStore.rooms.get(booking.getRoomId());
        if (room == null) {
            throw new RuntimeException("会议室不存在");
        }

        if (booking.getStartTime().isBefore(room.getOpenTime()) || 
            booking.getEndTime().isAfter(room.getCloseTime())) {
            throw new RuntimeException("预约时间不在会议室开放时间内");
        }

        if (booking.getAttendeeCount() > room.getCapacity()) {
            throw new RuntimeException("参会人数超过会议室容量");
        }

        if (booking.getRequiredEquipment() != null && !booking.getRequiredEquipment().isEmpty()) {
            for (String equipment : booking.getRequiredEquipment()) {
                if (!room.getEquipment().contains(equipment)) {
                    throw new RuntimeException("会议室缺少设备: " + equipment);
                }
            }
        }

        List<Booking> existingBookings = DataStore.getBookingsByRoomAndDate(booking.getRoomId(), booking.getDate());
        for (Booking existing : existingBookings) {
            if (isTimeConflict(booking.getStartTime(), booking.getEndTime(), 
                              existing.getStartTime(), existing.getEndTime())) {
                throw new RuntimeException("时间段与现有预约冲突");
            }
        }

        booking.setId(DataStore.bookingIdGenerator.getAndIncrement());
        booking.setRoomName(room.getName());
        booking.setCheckedIn(false);
        booking.setCreatedAt(LocalDateTime.now());

        if (booking.getImportant()) {
            booking.setStatus(BookingStatus.PENDING_APPROVAL);
        } else {
            booking.setStatus(BookingStatus.APPROVED);
            booking.setApprovedAt(LocalDateTime.now());
        }

        DataStore.bookings.put(booking.getId(), booking);
        return booking;
    }

    private boolean isTimeConflict(LocalTime start1, LocalTime end1, 
                                   LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    public Booking approveBooking(Long id) {
        Booking booking = DataStore.bookings.get(id);
        if (booking == null || booking.getStatus() != BookingStatus.PENDING_APPROVAL) {
            return null;
        }
        booking.setStatus(BookingStatus.APPROVED);
        booking.setApprovedAt(LocalDateTime.now());
        return booking;
    }

    public Booking rejectBooking(Long id) {
        Booking booking = DataStore.bookings.get(id);
        if (booking == null || booking.getStatus() != BookingStatus.PENDING_APPROVAL) {
            return null;
        }
        booking.setStatus(BookingStatus.REJECTED);
        return booking;
    }

    public Booking cancelBooking(Long id) {
        Booking booking = DataStore.bookings.get(id);
        if (booking == null) {
            return null;
        }
        if (booking.getStatus() == BookingStatus.CANCELLED || 
            booking.getStatus() == BookingStatus.REJECTED ||
            booking.getStatus() == BookingStatus.NO_SHOW) {
            return booking;
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        return booking;
    }

    public Booking checkIn(Long id) {
        Booking booking = DataStore.bookings.get(id);
        if (booking == null || booking.getStatus() != BookingStatus.APPROVED) {
            return null;
        }
        booking.setCheckedIn(true);
        return booking;
    }

    public List<Booking> getPendingApprovalBookings() {
        return DataStore.getPendingApprovalBookings();
    }
}
