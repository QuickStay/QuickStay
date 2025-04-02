package com.project.quickstay.eventListener;

import com.project.quickstay.common.Social;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.reservation.entity.DayReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.repository.UserRepository;
import com.project.quickstay.service.PlaceService;
import com.project.quickstay.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class EventListenerTest {

    @Autowired
    FakeReservationHandler reservationHandler;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    RoomService roomService;

    @Autowired
    PlaceRepository placeRepository;

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
        placeRegister.setProvince("강원도");
        placeRegister.setCity("춘천시");
        placeRegister.setDetailAddress("강원대학교");
        placeRegister.setContact("01012345678");

        place1 = placeService.register(user1, placeRegister);

        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.DAY);
        roomData.setCheckIn(LocalTime.of(15, 0));
        roomData.setCheckOut(LocalTime.of(11, 0));
        room = roomService.register(user1, place1.getId(), roomData);
    }

    @Test
    @DisplayName("TransactionalEventListner의 phase = TransactionPhase.AFTER_COMMIT에 대해 알아보기1")
    public void test1() {

        DayReservationRegister dayReservationRegister = new DayReservationRegister(user1, room);
        dayReservationRegister.setStartDate(LocalDate.of(2025, 2, 10));
        dayReservationRegister.setEndDate(LocalDate.of(2025, 2, 13));
        assertThatThrownBy(() -> reservationHandler.reservationRegister1_throwsException(user1, room.getId(), dayReservationRegister)).isInstanceOf(ServiceException.class);

        Place place = placeRepository.findById(place1.getId()).get();

        assertThat(place.getReservedCount()).isEqualTo(0);

        //예외 터졌을 시 실행되지 않음
    }

    @Test
    @DisplayName("TransactionalEventListner의 phase = TransactionPhase.AFTER_COMMIT에 대해 알아보기2")
    public void test2() {

        DayReservationRegister dayReservationRegister = new DayReservationRegister(user1, room);
        dayReservationRegister.setStartDate(LocalDate.of(2025, 2, 10));
        dayReservationRegister.setEndDate(LocalDate.of(2025, 2, 13));
        Reservation reservation = reservationHandler.reservationRegister2_normalOperation(user1, room.getId(), dayReservationRegister);

        Place place = placeRepository.findById(place1.getId()).get();

        assertThat(place.getReservedCount()).isEqualTo(1);

        //commit 되었을 경우 정상 작동
    }
}
