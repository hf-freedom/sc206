package com.meeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeetingRoomBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingRoomBookingApplication.class, args);
    }
}
