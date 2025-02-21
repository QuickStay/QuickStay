package com.project.quickstay.domain.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DayRegister {

    private LocalDate checkIn;
    private LocalDate checkOut;
}
