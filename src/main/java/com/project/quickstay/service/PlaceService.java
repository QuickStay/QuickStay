package com.project.quickstay.service;

import com.project.quickstay.domain.place.dto.*;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.dto.RoomInfo;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public PlaceUpdate getUpdateData(Long placeId) {
        Place place = getById(placeId);
        return PlaceUpdate.getUpdateData(place);
    }

    public void update(User user, Long placeId, PlaceUpdate placeUpdate) { //장소정보 수정
        Place place = getById(placeId);
        validUser(place.getUser(), user);
        place.update(placeUpdate);
    }

    public void delete(User user, Long placeId) {
        Place place = getById(placeId);
        validUser(place.getUser(), user);
        int count = roomRepository.getRoomCountByPlaceId(placeId);
        if (count != 0) {
            throw new ServiceException("방을 먼저 삭제해야 합니다.");
        }
        placeRepository.delete(place);
    }

    public PlaceInfo info(Long placeId) { //장소 상세
        Place place = getById(placeId);
        List<Room> rooms = roomRepository.findRoomsByPlaceId(placeId); //fetch join하여 프록시 객체말고 실제 객체로 들고오도록 설정
        List<RoomInfo> roomInfos = RoomInfo.roomList(rooms);
        return new PlaceInfo(place, roomInfos);
    }

    public List<MyPlaceInfo> getMyPlace(User user) {
        return placeRepository.getMyPlace(user.getId());
    }

    public List<PlaceSearch> search(String keyword, Long placeId) {
        return placeRepository.search(placeId, keyword, 10);
    }

    private void validUser(User user1, User user2) {
        if (!user1.equals(user2)) {
            throw new ServiceException("유저 검증에 실패하였습니다.");
        }
    }

    private Place getById(Long id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new ServiceException("장소가 없습니다.");
        }
        return place.get();
    }
}
