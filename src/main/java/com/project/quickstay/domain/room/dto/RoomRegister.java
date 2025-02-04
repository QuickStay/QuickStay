package com.project.quickstay.domain.room.dto;

import com.project.quickstay.common.BookType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class RoomRegister {

    private String name;
    private String description;
    private int capacity;


    private BookType bookType;

    //DAY
    private LocalTime checkIn;
    private LocalTime checkOut;

    //TIME
    private LocalTime startTime;
    private LocalTime endTime;
}
