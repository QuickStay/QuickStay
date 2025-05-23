package com.project.quickstay.service;

import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.dto.MyRoomInfo;
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
public class RoomService {

    private final PlaceRepository placeRepository;
    private final RoomRepository roomRepository;

    public Room register(User user, Long placeId, RoomData roomData) {
        Place place = getPlaceById(placeId);
        if (!place.getUser().getId().equals(user.getId())) {
            throw new ServiceException("유저 검증에 실패하였습니다.");
        }
        Room room = Room.register(place, roomData);
        return roomRepository.save(room);
    }

    public RoomData getUpdateData(Long roomId) {
        Room room = getRoomById(roomId);
        return room.getUpdateData();
    }

    public void update(User user, Long roomId, RoomData roomData) { //룸 정보 변경
        validUser(user, roomId);
        Room room = getRoomById(roomId);
        room.updateRoom(roomData);
    }

    public void delete(User user, Long roomId) {
        validUser(user, roomId);
        Room room = getRoomById(roomId);
        roomRepository.delete(room);
    }

    public List<MyRoomInfo> getMyRoom(Long placeId) {
        return roomRepository.findMyRoomByPlaceId(placeId);
    }

    public Room findById(Long roomId) {
        return getRoomById(roomId);
    }

    public void validUser(User user, Long roomId) {
        Room room = getRoomById(roomId);
        if (!room.getPlace().getUser().equals(user)) {
            throw new ServiceException("유저 검증에 실패하였습니다.");
        }
    }

    private Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new ServiceException("방이 없습니다."));
    }

    private Place getPlaceById(Long id) {
        return placeRepository.findById(id).orElseThrow(() -> new ServiceException("장소가 없습니다."));
    }
}
