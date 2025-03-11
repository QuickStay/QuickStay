package com.project.quickstay.domain.place.dto;

import com.project.quickstay.domain.place.entity.Place;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceUpdate {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private String address;
    @NotEmpty
    private String contact;

    public static PlaceUpdate getUpdateData(Place place) {
        PlaceUpdate placeUpdate = new PlaceUpdate();
        placeUpdate.name = place.getName();
        placeUpdate.description = place.getDescription();
        placeUpdate.address = place.getAddress();
        placeUpdate.contact = place.getContact();
        return placeUpdate;
    }

}
