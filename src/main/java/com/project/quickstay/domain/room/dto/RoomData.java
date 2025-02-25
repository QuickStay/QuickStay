package com.project.quickstay.domain.room.dto;

import com.project.quickstay.domain.room.entity.BookType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalTime;

@Getter
@Setter
public class RoomData {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @Range(min = 1, max = 10)
    private int capacity;

    @NotNull
    private BookType bookType;

    //DAY
    private LocalTime checkIn;
    private LocalTime checkOut;

    //TIME
    private LocalTime startTime;
    private LocalTime endTime;
}
