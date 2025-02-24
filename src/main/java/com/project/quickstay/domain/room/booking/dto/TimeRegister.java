package com.project.quickstay.domain.room.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TimeRegister {
    private LocalTime startTime;
    private LocalTime endTime;
}
