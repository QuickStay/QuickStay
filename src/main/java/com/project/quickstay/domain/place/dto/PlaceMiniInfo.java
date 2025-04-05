package com.project.quickstay.domain.place.dto;

import com.project.quickstay.domain.place.entity.Place;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class PlaceMiniInfo {

    private Long placeId;
    private String name;
    private String address;

    private String rankChanges; //순위 변동

    public PlaceMiniInfo(Place place) {
        this.placeId = place.getId();
        this.name = place.getName();
        this.address = place.getProvince() + " " + place.getCity();
    }

    public PlaceMiniInfo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceMiniInfo that = (PlaceMiniInfo) o;

        return Objects.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return placeId != null ? placeId.hashCode() : 0;
    }
}
