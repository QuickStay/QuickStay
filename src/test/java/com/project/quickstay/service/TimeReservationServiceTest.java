package com.project.quickstay.service;

import com.project.quickstay.common.Social;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.reservation.entity.TimeReservationRegister;
import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TimeReservationServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    RoomService roomService;

    @Autowired
    ReservationHandler reservationHandler;

    User user1;
    Place place1;
    Room room;

    @BeforeEach
    void setPlace() {
        UserRegister userRegister = new UserRegister();
        userRegister.setEmail("tkddnr@naver.com");
        userRegister.setNickname("상욱");
        userRegister.setSocial(Social.KAKAO);
        User user = User.register(userRegister);
        user1 = userRepository.save(user);

        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setAddress("강원도 춘천시");
        placeRegister.setContact("01012345678");

        place1 = placeService.register(user1, placeRegister);

        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.TIME);
        roomData.setStartTime(LocalTime.of(11, 0));
        roomData.setEndTime(LocalTime.of(18, 0));
        room = roomService.register(user1, place1.getId(), roomData);
    }

    @Test
    @DisplayName("예약 불가능한 시간 가져오기 - 1")
    void test1() {
        TimeReservationRegister timeReservationRegister = new TimeReservationRegister(user1, room);
        timeReservationRegister.setStartTime(LocalTime.of(13, 0));
        timeReservationRegister.setEndTime(LocalTime.of(14, 0));
        timeReservationRegister.setDate(LocalDate.of(2025, 4, 7));

        reservationHandler.reservationRegister(user1, room.getId(), timeReservationRegister);

        List<LocalDateTime> reserved = (List<LocalDateTime>) reservationHandler.getReserved(room.getId());

        System.out.println("reserved = " + reserved);
//        assertThat()
    }
}
