package com.project.quickstay.repository;

import com.project.quickstay.common.Social;
import com.project.quickstay.domain.place.dto.PlaceInfo;
import com.project.quickstay.domain.place.dto.PlaceMiniInfo;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.reservation.entity.DayReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.review.dto.ReviewWrite;
import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.repository.UserRepository;
import com.project.quickstay.service.PlaceService;
import com.project.quickstay.service.ReservationHandler;
import com.project.quickstay.service.ReviewService;
import com.project.quickstay.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MainPageRecommendationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    RoomService roomService;

    @Autowired
    ReservationHandler reservationHandler;

    @Autowired
    ReviewService reviewService;

    @Autowired
    PlaceRepository placeRepository;

    User user1;
    Place place1;
    Place place2;
    Room room1;
    Room room2;

    @BeforeEach
    public void setData() {
        UserRegister userRegister = new UserRegister();
        userRegister.setEmail("tkddnr@naver.com");
        userRegister.setNickname("상욱");
        userRegister.setSocial(Social.KAKAO);
        User user = User.register(userRegister);
        user1 = userRepository.save(user);

        PlaceRegister placeRegister1 = new PlaceRegister();
        placeRegister1.setName("한옥");
        placeRegister1.setDescription("우아한 한옥입니다");
        placeRegister1.setProvince("강원도");
        placeRegister1.setCity("춘천시");
        placeRegister1.setDetailAddress("강원대학교");
        placeRegister1.setContact("01012345678");

        place1 = placeService.register(user1, placeRegister1);

        PlaceRegister placeRegister2 = new PlaceRegister();
        placeRegister2.setName("파티룸");
        placeRegister2.setDescription("근사한 파티룸");
        placeRegister2.setProvince("강원도");
        placeRegister2.setCity("춘천시");
        placeRegister2.setDetailAddress("후평동");
        placeRegister2.setContact("01031114444");

        place2 = placeService.register(user1, placeRegister2);

        RoomData roomData1 = new RoomData();
        roomData1.setName("방1");
        roomData1.setDescription("넓은 방");
        roomData1.setCapacity(4);

        roomData1.setBookType(BookType.DAY);
        roomData1.setCheckIn(LocalTime.of(15, 0));
        roomData1.setCheckOut(LocalTime.of(11, 0));
        room1 = roomService.register(user1, place1.getId(), roomData1);

        RoomData roomData2 = new RoomData();
        roomData2.setName("방1");
        roomData2.setDescription("넓은 방");
        roomData2.setCapacity(4);

        roomData2.setBookType(BookType.DAY);
        roomData2.setCheckIn(LocalTime.of(15, 0));
        roomData2.setCheckOut(LocalTime.of(11, 0));
        room2 = roomService.register(user1, place2.getId(), roomData2);
    }

    @Test
    @DisplayName("오늘 가장 많이 예약된 숙소 리스트를 가져올 수 있다")
    public void test1() {
        //given
        //place1만 예약
        DayReservationRegister dayReservationRegister = new DayReservationRegister(user1, room1);
        dayReservationRegister.setStartDate(LocalDate.of(2025, 2, 10));
        dayReservationRegister.setEndDate(LocalDate.of(2025, 2, 13));

        reservationHandler.reservationRegister(user1, room1.getId(), dayReservationRegister);

        //when
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<PlaceMiniInfo> place = placeRepository.findTenTodayMostReservedPlace(startOfDay, endOfDay);

        //then
        assertThat(place).size().isEqualTo(1);
        PlaceMiniInfo placeMiniInfo = place.get(0);
        assertThat(placeMiniInfo.getPlaceId()).isEqualTo(place1.getId());
        assertThat(placeMiniInfo.getName()).isEqualTo(place1.getName());
        assertThat(placeMiniInfo.getAddress()).isEqualTo(place1.getProvince() + " " + place1.getCity());
    }

    @Test
    @DisplayName("예약이 가장 많은 숙소를 가져올 수 있다")
    public void test2() {
        //given
        //place1 2번 예약, place2 1번 예약
        DayReservationRegister r1 = new DayReservationRegister(user1, room1);
        r1.setStartDate(LocalDate.of(2025, 2, 10));
        r1.setEndDate(LocalDate.of(2025, 2, 13));

        DayReservationRegister r2 = new DayReservationRegister(user1, room1);
        r2.setStartDate(LocalDate.of(2025, 2, 8));
        r2.setEndDate(LocalDate.of(2025, 2, 9));

        reservationHandler.reservationRegister(user1, room1.getId(), r1);
        reservationHandler.reservationRegister(user1, room1.getId(), r2);

        DayReservationRegister r3 = new DayReservationRegister(user1, room2);
        r3.setStartDate(LocalDate.of(2025, 2, 8));
        r3.setEndDate(LocalDate.of(2025, 2, 9));
        reservationHandler.reservationRegister(user1, room2.getId(), r3);

        //when
        List<PlaceMiniInfo> placeInfo = placeRepository.findFiveMostReservedPlace();

        //then
        assertThat(placeInfo).size().isEqualTo(2);
        PlaceMiniInfo info1 = placeInfo.get(0);
        assertThat(info1.getPlaceId()).isEqualTo(place1.getId());
        assertThat(info1.getName()).isEqualTo(place1.getName());
        assertThat(info1.getAddress()).isEqualTo(place1.getProvince() + " " + place1.getCity());

        PlaceMiniInfo info2 = placeInfo.get(1);
        assertThat(info2.getPlaceId()).isEqualTo(place2.getId());
        assertThat(info2.getName()).isEqualTo(place2.getName());
        assertThat(info2.getAddress()).isEqualTo(place2.getProvince() + " " + place2.getCity());

    }

    @Test
    @DisplayName("평점이 가장 높은 숙소를 가져올 수 있다")
    public void test3() {
        //given
        DayReservationRegister r1 = new DayReservationRegister(user1, room1);
        r1.setStartDate(LocalDate.of(2025, 2, 10));
        r1.setEndDate(LocalDate.of(2025, 2, 13));
        reservationHandler.reservationRegister(user1, room1.getId(), r1);

        DayReservationRegister r3 = new DayReservationRegister(user1, room2);
        r3.setStartDate(LocalDate.of(2025, 2, 8));
        r3.setEndDate(LocalDate.of(2025, 2, 9));
        reservationHandler.reservationRegister(user1, room2.getId(), r3);

        ReviewWrite review1 = new ReviewWrite();
        review1.setRoomName(room1.getName());
        review1.setPlaceId(place1.getId());
        review1.setContent("음 별로에요");
        review1.setScore(2);
        reviewService.writeReview(review1, user1);

        ReviewWrite review2 = new ReviewWrite();
        review2.setRoomName(room2.getName());
        review2.setPlaceId(place2.getId());
        review2.setContent("좋아요");
        review2.setScore(5);
        reviewService.writeReview(review2, user1);

        //when
        List<PlaceMiniInfo> placeInfo = placeRepository.findFiveHighestReviewAveragePlace();

        //then
        assertThat(placeInfo).size().isEqualTo(2);
        PlaceMiniInfo info1 = placeInfo.get(0);
        assertThat(info1.getPlaceId()).isEqualTo(place2.getId());
        assertThat(info1.getName()).isEqualTo(place2.getName());
        assertThat(info1.getAddress()).isEqualTo(place2.getProvince() + " " + place2.getCity());

        PlaceMiniInfo info2 = placeInfo.get(1);
        assertThat(info2.getPlaceId()).isEqualTo(place1.getId());
        assertThat(info2.getName()).isEqualTo(place1.getName());
        assertThat(info2.getAddress()).isEqualTo(place1.getProvince() + " " + place1.getCity());

    }

    @Test
    @DisplayName("랜덤한 숙소 추천 목록을 가져올 수 있다")
    public void test4() {
        List<Place> place = placeRepository.findFiveRandomPlace();
        List<PlaceMiniInfo> placeInfo = place.stream().map(PlaceMiniInfo::new).toList();
        assertThat(placeInfo).size().isEqualTo(2);
    }
}
