package com.project.quickstay.domain.place.dto;

import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.dto.RoomInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaceInfo {

    private Long placeId;
    private Long ownerId;
    private String owner;
    private String name;
    private String description;
    private String address;
    private String contact;

    private int roomCount; //장소에 방이 몇개인지
    private List<RoomInfo> rooms;

    public PlaceInfo(Place place, List<RoomInfo> rooms) {
        placeId = place.getId();
        ownerId = place.getUser().getId();
        owner = place.getUser().getNickname();
        name = place.getName();
        description = place.getDescription();
        address = place.getProvince() + " " + place.getCity() + " " + place.getDetailAddress();
        contact = place.getContact();

        roomCount = place.getRooms().size();
        this.rooms = rooms;
    }
}
