package com.project.quickstay.domain.place.dto;

import com.project.quickstay.domain.place.entity.Place;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceMiniInfo {

    private Long placeId;
    private String name;
    private String address;

    public PlaceMiniInfo(Place place) {
        this.placeId = place.getId();
        this.name = place.getName();
        this.address = place.getProvince() + " " + place.getCity();
    }
}
