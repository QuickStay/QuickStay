package com.project.quickstay.service;

import com.project.quickstay.common.Constant;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final RoomRepository roomRepository;
    private final RedisService redisService;

    public Place register(User user, PlaceRegister placeRegister) { //장소 등록
        canAddPlace(user.getId());
        Place place = Place.register(user, placeRegister);
        return placeRepository.save(place);
    }

    public PlaceUpdate getUpdateData(Long placeId) {
        Place place = getById(placeId);
        return PlaceUpdate.getUpdateData(place);
    }

    public void update(User user, Long placeId, PlaceUpdate placeUpdate) { //장소정보 수정
        validUser(placeId, user);
        Place place = getById(placeId);
        place.update(placeUpdate);
    }

    public void delete(User user, Long placeId) {
        validUser(placeId, user);
        Place place = getById(placeId);
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

    public void validUser(Long placeId, User user2) {
        Place place = getById(placeId);
        if (!place.getUser().equals(user2)) {
            throw new ServiceException("권한이 없습니다.");
        }
    }

    //TODO: redis 캐싱 도입, 스케줄러를 사용하여 매 정각마다 redis의 데이터를 갱신 -> DB를 아예 거치지 않고 캐싱된 결과만 보여주도록 변경 필요
    public List<PlaceMiniInfo> findFiveMostReservedPlace() {
        List<PlaceMiniInfo> mostReserved = placeRepository.findFiveMostReservedPlace();
        redisService.saveMainPageData(Constant.MOST_RESERVED, mostReserved);
        return redisService.getValue(Constant.MOST_RESERVED);
    }

    public List<PlaceMiniInfo> findFiveHighestReviewAveragePlace() {
        List<PlaceMiniInfo> highest = placeRepository.findFiveHighestReviewAveragePlace();
        redisService.saveMainPageData(Constant.HIGHEST_REVIEW_AVERAGE, highest);
        return redisService.getValue(Constant.HIGHEST_REVIEW_AVERAGE);
    }

    public List<PlaceMiniInfo> findFiveRandomPlace() {
        List<Place> temp = placeRepository.findFiveRandomPlace();
        List<PlaceMiniInfo> toList = temp.stream().map(PlaceMiniInfo::new).toList();
        redisService.saveMainPageData(Constant.RANDOM, toList);
        return redisService.getValue(Constant.RANDOM);
    }

    public List<PlaceMiniInfo> findTenTodayMostReservedPlace() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<PlaceMiniInfo> toList = placeRepository.findTenTodayMostReservedPlace(startOfDay, endOfDay)
                .stream()
                .map(PlaceMiniInfo::new)
                .toList();
        redisService.saveMainPageData(Constant.TODAY_MOST_RESERVED, toList);
        return redisService.getValue(Constant.TODAY_MOST_RESERVED);
    }

    private Place getById(Long id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new ServiceException("장소가 없습니다.");
        }
        return place.get();
    }

    private void canAddPlace(Long userId) {
        int count = placeRepository.countByUserId(userId);
        if (count >= Constant.MAX_PLACE_COUNT) {
            throw new ServiceException("최대 장소 소유 개수를 초과하였습니다.");
        }
    }
}
