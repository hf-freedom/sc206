package com.meeting.model;

import java.util.Map;

public class Statistics {
    private Double usageRate;
    private Double cancellationRate;
    private Double noShowRate;
    private Integer totalBookings;
    private Integer completedBookings;
    private Integer cancelledBookings;
    private Integer noShowBookings;
    private Map<Integer, Long> popularTimeSlots;
    private Map<String, Long> roomUsage;

    public Double getUsageRate() { return usageRate; }
    public void setUsageRate(Double usageRate) { this.usageRate = usageRate; }
    public Double getCancellationRate() { return cancellationRate; }
    public void setCancellationRate(Double cancellationRate) { this.cancellationRate = cancellationRate; }
    public Double getNoShowRate() { return noShowRate; }
    public void setNoShowRate(Double noShowRate) { this.noShowRate = noShowRate; }
    public Integer getTotalBookings() { return totalBookings; }
    public void setTotalBookings(Integer totalBookings) { this.totalBookings = totalBookings; }
    public Integer getCompletedBookings() { return completedBookings; }
    public void setCompletedBookings(Integer completedBookings) { this.completedBookings = completedBookings; }
    public Integer getCancelledBookings() { return cancelledBookings; }
    public void setCancelledBookings(Integer cancelledBookings) { this.cancelledBookings = cancelledBookings; }
    public Integer getNoShowBookings() { return noShowBookings; }
    public void setNoShowBookings(Integer noShowBookings) { this.noShowBookings = noShowBookings; }
    public Map<Integer, Long> getPopularTimeSlots() { return popularTimeSlots; }
    public void setPopularTimeSlots(Map<Integer, Long> popularTimeSlots) { this.popularTimeSlots = popularTimeSlots; }
    public Map<String, Long> getRoomUsage() { return roomUsage; }
    public void setRoomUsage(Map<String, Long> roomUsage) { this.roomUsage = roomUsage; }
}
