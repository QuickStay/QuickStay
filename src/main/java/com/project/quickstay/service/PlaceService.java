package com.project.quickstay.service;

import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place register(User user, PlaceRegister placeRegister) { //장소 등록
        Place place = Place.register(user, placeRegister);
        return placeRepository.save(place);
    }
}
