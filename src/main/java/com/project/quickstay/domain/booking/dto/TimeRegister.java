package com.project.quickstay.domain.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TimeRegister {
    private LocalTime startTime;
    private LocalTime endTime;
}
