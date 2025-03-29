package com.project.quickstay.domain.reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class OperatingHours {

    private LocalTime startTime;
    private LocalTime endTime;

    public OperatingHours(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
