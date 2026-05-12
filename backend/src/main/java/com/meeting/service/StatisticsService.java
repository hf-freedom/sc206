package com.meeting.service;

import com.meeting.model.Booking;
import com.meeting.model.BookingStatus;
import com.meeting.model.Statistics;
import com.meeting.repository.DataStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    public Statistics getStatistics(LocalDate startDate, LocalDate endDate) {
        List<Booking> allBookings = DataStore.bookings.values().stream()
            .filter(b -> !b.getDate().isBefore(startDate) && !b.getDate().isAfter(endDate))
            .collect(Collectors.toList());

        Statistics stats = new Statistics();
        
        int total = allBookings.size();
        stats.setTotalBookings(total);

        long completed = allBookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.APPROVED && b.getCheckedIn())
            .count();
        stats.setCompletedBookings((int) completed);

        long cancelled = allBookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
            .count();
        stats.setCancelledBookings((int) cancelled);

        long noShow = allBookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.NO_SHOW)
            .count();
        stats.setNoShowBookings((int) noShow);

        if (total > 0) {
            stats.setUsageRate((double) completed / total * 100);
            stats.setCancellationRate((double) cancelled / total * 100);
            stats.setNoShowRate((double) noShow / total * 100);
        } else {
            stats.setUsageRate(0.0);
            stats.setCancellationRate(0.0);
            stats.setNoShowRate(0.0);
        }

        Map<Integer, Long> timeSlots = allBookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.APPROVED || b.getStatus() == BookingStatus.COMPLETED)
            .collect(Collectors.groupingBy(
                b -> b.getStartTime().getHour(),
                Collectors.counting()
            ));
        stats.setPopularTimeSlots(timeSlots);

        Map<String, Long> roomUsage = allBookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.APPROVED || b.getStatus() == BookingStatus.COMPLETED)
            .collect(Collectors.groupingBy(
                Booking::getRoomName,
                Collectors.counting()
            ));
        stats.setRoomUsage(roomUsage);

        return stats;
    }
}
