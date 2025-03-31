package com.project.quickstay.domain.place.entity;

import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.dto.PlaceUpdate;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Room> rooms;

    private String name;

    private String description;

    private String address;

    private String contact;

    private int reservedCount;

    private int reviewCount;

    private float reviewAverage;

    public Place() {
    }

    public static Place register(User user, PlaceRegister placeRegister) {
        Place place = new Place();
        place.user = user;
        place.name = placeRegister.getName();
        place.description = placeRegister.getDescription();
        place.address = placeRegister.getAddress();
        place.contact = placeRegister.getContact();
        place.reservedCount = 0;
        place.reviewCount = 0;
        place.reviewAverage = 0;
        return place;
    }

    public void update(PlaceUpdate placeUpdate) {
        this.name = placeUpdate.getName();
        this.description = placeUpdate.getDescription();
        this.address = placeUpdate.getAddress();
        this.contact = placeUpdate.getContact();
    }

    public void plusReservedCount() {
        reservedCount++;
    }

    public void minusReservedCount() {
        reservedCount--;
    }

    public void reviewAdded(int score) {
        if (this.reviewCount == 0) {
            this.reviewAverage = score;
        } else {
            this.reviewAverage = Math.round(((reviewAverage * reviewCount) + score) / (reviewCount + 1) * 100) / 100.0f;
        }

        this.reviewCount++;
    }

    public void reviewDeleted(int score) {
        this.reviewAverage = Math.round(((reviewAverage * reviewCount) - score) / (reviewCount - 1) * 100) / 100.0f;
        this.reviewCount -= 1;
    }
}
