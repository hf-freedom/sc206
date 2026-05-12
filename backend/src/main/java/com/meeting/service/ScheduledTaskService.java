package com.meeting.service;

import com.meeting.model.Booking;
import com.meeting.model.BookingStatus;
import com.meeting.repository.DataStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ScheduledTaskService {

    @Value("${meeting.approval.timeout-minutes:30}")
    private int approvalTimeoutMinutes;

    @Value("${meeting.check-in.timeout-minutes:10}")
    private int checkInTimeoutMinutes;

    @Scheduled(fixedRate = 60000)
    public void checkApprovalTimeout() {
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : DataStore.bookings.values()) {
            if (booking.getStatus() == BookingStatus.PENDING_APPROVAL) {
                long minutesElapsed = ChronoUnit.MINUTES.between(booking.getCreatedAt(), now);
                if (minutesElapsed >= approvalTimeoutMinutes) {
                    booking.setStatus(BookingStatus.CANCELLED);
                    booking.setCancelledAt(now);
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkNoShow() {
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : DataStore.bookings.values()) {
            if (booking.getStatus() == BookingStatus.APPROVED && !booking.getCheckedIn()) {
                LocalDateTime meetingStart = LocalDateTime.of(booking.getDate(), booking.getStartTime());
                long minutesElapsed = ChronoUnit.MINUTES.between(meetingStart, now);
                if (minutesElapsed >= checkInTimeoutMinutes) {
                    booking.setStatus(BookingStatus.NO_SHOW);
                }
            }
        }
    }
}
