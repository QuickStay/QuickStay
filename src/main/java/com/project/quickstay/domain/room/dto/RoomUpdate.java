package com.project.quickstay.domain.room.dto;

import com.project.quickstay.common.BookType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
public class RoomUpdate {

    private String name;
    private String description;
    private int capacity;

    private BookType bookType;

    private LocalTime checkIn;
    private LocalTime checkOut;

    private LocalTime startTime;
    private LocalTime endTime;
}
