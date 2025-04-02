package com.project.quickstay.domain.place.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceSearch {

    private Long id;
    private String owner;
    private String name;
    private String description;
    private String address;

    @QueryProjection
    public PlaceSearch(Long id, String owner, String name, String description, String province, String city, String detailAddress) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.address = province + " " + city + " " + detailAddress;
    }
}
