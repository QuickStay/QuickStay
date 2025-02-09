package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.common.Social;
import com.project.quickstay.common.State;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.reservation.dto.DayReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.dto.RoomRegister;
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
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DayReservationServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    RoomService roomService;

    @Autowired
    DayReservationService dayReservationService;

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

        RoomRegister roomRegister = new RoomRegister();
        roomRegister.setName("방1");
        roomRegister.setDescription("넓은 방");
        roomRegister.setCapacity(4);

        roomRegister.setBookType(BookType.DAY);
        roomRegister.setCheckIn(LocalTime.of(15, 0));
        roomRegister.setCheckOut(LocalTime.of(11, 0));
        room = roomService.register(user1, place1.getId(), roomRegister);
    }

    @Test
    @DisplayName("사용자는 방을 예약할 수 있다 - DAY")
    void test1() {
        DayReservationRegister dayReservationRegister = new DayReservationRegister();
        dayReservationRegister.setStartDate(LocalDate.of(2025, 2, 10));
        dayReservationRegister.setEndDate(LocalDate.of(2025, 2, 13));

        Reservation reservation = dayReservationService.registerDayReservation(user1, room.getId(), dayReservationRegister);

        assertThat(reservation.getId()).isNotNull();
        assertThat(reservation.getRoom()).isEqualTo(room);
        assertThat(reservation.getUser()).isEqualTo(user1);
        assertThat(reservation.getStartDate()).isEqualTo(LocalDate.of(2025, 2, 10));
        assertThat(reservation.getEndDate()).isEqualTo(LocalDate.of(2025, 2, 13));
        assertThat(reservation.getState()).isEqualTo(State.RESERVED);
        assertThat(reservation.getStartTime()).isNull();
        assertThat(reservation.getEndTime()).isNull();
    }

//    @Test
//    @DisplayName("roomId로 방에 대한 예약 목록을 가져올 수 있다")
//    void test2() {
//
//        DayReservationRegister dayReservationRegister = new DayReservationRegister();
//        dayReservationRegister.setStartDate(LocalDate.of(2025, 2, 10));
//        dayReservationRegister.setEndDate(LocalDate.of(2025, 2, 13));
//
//        reservationService.registerDayReservation(user1, room.getId(), dayReservationRegister);
//
//        List<Reservation> allReservations = reservationService.getAllReservations(room.getId());
//        assertThat(allReservations.size()).isEqualTo(1);
//        Reservation reservation = allReservations.get(0);
//        assertThat(reservation.getId()).isNotNull();
//        assertThat(reservation.getRoom()).isEqualTo(room);
//        assertThat(reservation.getUser()).isEqualTo(user1);
//        assertThat(reservation.getStartDate()).isEqualTo(LocalDate.of(2025, 2, 10));
//        assertThat(reservation.getEndDate()).isEqualTo(LocalDate.of(2025, 2, 13));
//        assertThat(reservation.getState()).isEqualTo(State.RESERVED);
//        assertThat(reservation.getStartTime()).isNull();
//        assertThat(reservation.getEndTime()).isNull();
//    }

    @Test
    @DisplayName("예약 불가능한 날짜 가져오기 - 1. 현재 날짜보다 넘는 예약 불가능한 날짜를 가져올 수 있다")
    void test3() {
        DayReservationRegister dayReservationRegister = new DayReservationRegister();
        dayReservationRegister.setStartDate(LocalDate.of(2026, 2, 10));
        dayReservationRegister.setEndDate(LocalDate.of(2026, 2, 13));

        dayReservationService.registerDayReservation(user1, room.getId(), dayReservationRegister);

        List<LocalDate> reservedDate = dayReservationService.getReservedDate(room.getId());
        assertThat(reservedDate.size()).isEqualTo(3);
        Collections.sort(reservedDate);
        int startDay = 10;
        for (LocalDate localDate : reservedDate) {
            assertThat(localDate).isEqualTo(LocalDate.of(2026, 2, startDay));
            startDay++;
        }
    }

    @Test
    @DisplayName("예약 불가능한 날짜 가져오기 - 2. 현재 날짜보다 지난 예약 불가능한 날짜는 가져오지 않는다")
    void test4() {
        DayReservationRegister dayReservationRegister = new DayReservationRegister();
        dayReservationRegister.setStartDate(LocalDate.of(2024, 2, 10));
        dayReservationRegister.setEndDate(LocalDate.of(2024, 2, 13));

        dayReservationService.registerDayReservation(user1, room.getId(), dayReservationRegister);

        List<LocalDate> reservedDate = dayReservationService.getReservedDate(room.getId());

        assertThat(reservedDate.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("예약 불가능한 날짜 가져오기 - 3. 현재 날짜보다 넘는 예약일지라도 예약 취소된 날짜는 가져오지 않는다")
    void test5() {

    }

}