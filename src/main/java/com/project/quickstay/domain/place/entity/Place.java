package com.project.quickstay.domain.place.entity;

import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String name;

    private String description;

    private String address;

    private String contact;

    public Place() {
    }

    public static Place register(User user, PlaceRegister placeRegister) {
        Place place = new Place();
        place.user = user;
        place.name = placeRegister.getName();
        place.description = placeRegister.getDescription();
        place.address = placeRegister.getAddress();
        place.contact = placeRegister.getContact();
        return place;
    }
}
