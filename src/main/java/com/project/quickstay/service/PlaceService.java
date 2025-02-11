package com.project.quickstay.service;

import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final RoomRepository roomRepository;

    public Place register(User user, PlaceRegister placeRegister) { //장소 등록
        Place place = Place.register(user, placeRegister);
        return placeRepository.save(place);
    }

    public PlaceRegister getUpdateData(Long placeId) {
        Place place = getById(placeId);
        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName(place.getName());
        placeRegister.setDescription(place.getDescription());
        placeRegister.setAddress(place.getAddress());
        placeRegister.setContact(place.getContact());
        return placeRegister;
    }

    public void update(User user, Long placeId, PlaceRegister placeRegister) { //장소정보 수정
        Place place = getById(placeId);
        validUser(place.getUser(), user);
        place.update(placeRegister);
    }

    public void delete(User user, Long placeId) {
        Place place = getById(placeId);
        validUser(place.getUser(), user);
        int count = roomRepository.getRoomCountByPlaceId(placeId);
        if (count != 0) {
            throw new IllegalStateException(); //FIXME
        }
        placeRepository.delete(place);
    }

    private void validUser(User user1, User user2) {
        if (!user1.equals(user2)) {
            throw new IllegalStateException(); //FIXME
        }
    }

    private Place getById(Long id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        return place.get();
    }
}
