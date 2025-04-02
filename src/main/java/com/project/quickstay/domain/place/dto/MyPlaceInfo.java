package com.project.quickstay.domain.place.dto;

import com.project.quickstay.domain.place.entity.Place;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPlaceInfo {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String contact;

    private int roomCount;

    public MyPlaceInfo(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.description = place.getDescription();
        this.address = place.getProvince() + " " + place.getCity() + " " + place.getDetailAddress();
        this.contact = place.getContact();
        this.roomCount = place.getRooms().size();
    }
}
