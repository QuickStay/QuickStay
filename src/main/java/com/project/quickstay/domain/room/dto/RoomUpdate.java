package com.project.quickstay.domain.room.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomUpdate {

    private String name;
    private String description;
    private int capacity;
}
