package com.project.quickstay.service;

import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.common.Social;
import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.room.entity.Booking;
import com.project.quickstay.domain.room.entity.DayBooking;
import com.project.quickstay.domain.room.entity.TimeBooking;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.RoomRepository;
import com.project.quickstay.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    User user1;
    Place place1;

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
    }

    @Test
    @DisplayName("사용자는 장소에 방을 등록할 수 있다 (DAY)")
    void test1() {
        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.DAY);
        roomData.setCheckIn(LocalTime.of(15, 0));
        roomData.setCheckOut(LocalTime.of(11, 0));
        Room room = roomService.register(user1, place1.getId(), roomData);

        assertThat(room.getId()).isNotNull();
        assertThat(room.getPlace()).isEqualTo(place1);
        assertThat(room.getName()).isEqualTo("방1");
        assertThat(room.getDescription()).isEqualTo("넓은 방");
        assertThat(room.getCapacity()).isEqualTo(4);
        assertThat(room.getBooking()).isInstanceOf(DayBooking.class);
    }

    @Test
    @DisplayName("사용자는 장소에 방을 등록할 수 있다 (TIME)")
    void test2() {
        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.TIME);
        roomData.setStartTime(LocalTime.of(12, 0));
        roomData.setEndTime(LocalTime.of(23, 0));
        Room room = roomService.register(user1, place1.getId(), roomData);

        assertThat(room.getId()).isNotNull();
        assertThat(room.getPlace()).isEqualTo(place1);
        assertThat(room.getName()).isEqualTo("방1");
        assertThat(room.getDescription()).isEqualTo("넓은 방");
        assertThat(room.getCapacity()).isEqualTo(4);
        assertThat(room.getBooking()).isInstanceOf(TimeBooking.class);
    }

    /**
     * Controller에서 예외 처리
     */
//    @Test
//    @DisplayName("방을 등록할 때 시간이 잘못되면 예외를 터트린다 (TIME) - 1. 시작시간보다 종료시간이 많을 경우")
//    void test3() {
//        RoomRegister roomRegister = new RoomRegister();
//        roomRegister.setName("방1");
//        roomRegister.setDescription("넓은 방");
//        roomRegister.setCapacity(4);
//
//        roomRegister.setBookType(BookType.TIME);
//        roomRegister.setStartTime(LocalTime.of(10, 0)); //시작시간 10시
//        roomRegister.setEndTime(LocalTime.of(9, 0)); //종료시간 9시
//
//        assertThatThrownBy(() -> roomService.register(place1.getId(), roomRegister)).isInstanceOf(IllegalStateException.class);
//
//    }
//
//    @Test
//    @DisplayName("방을 등록할 때 시간이 잘못되면 예외를 터트린다 (TIME) - 2. 시작시간과 종료시간이 같은 경우")
//    void test4() {
//        RoomRegister roomRegister = new RoomRegister();
//        roomRegister.setName("방1");
//        roomRegister.setDescription("넓은 방");
//        roomRegister.setCapacity(4);
//
//        roomRegister.setBookType(BookType.TIME);
//        roomRegister.setStartTime(LocalTime.of(9, 0)); //시작시간 9시
//        roomRegister.setEndTime(LocalTime.of(9, 0)); //종료시간 9시
//
//        assertThatThrownBy(() -> roomService.register(place1.getId(), roomRegister)).isInstanceOf(IllegalStateException.class);
//
//    }
//
//    @Test
//    @DisplayName("방을 등록할 때 시간이 잘못되면 예외를 터트린다 (TIME) - 3. 시작시간 + 1시간 > 종료시간인 경우")
//    void test5() {
//        RoomRegister roomRegister = new RoomRegister();
//        roomRegister.setName("방1");
//        roomRegister.setDescription("넓은 방");
//        roomRegister.setCapacity(4);
//
//        roomRegister.setBookType(BookType.TIME);
//        roomRegister.setStartTime(LocalTime.of(9, 0)); //시작시간 9시
//        roomRegister.setEndTime(LocalTime.of(9, 30)); //종료시간 9시 30분
//
//        assertThatThrownBy(() -> roomService.register(place1.getId(), roomRegister)).isInstanceOf(IllegalStateException.class);
//
//    }

    @Test
    @DisplayName("등록한 방을 수정할 수 있다")
    void test6() {
        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.TIME);
        roomData.setStartTime(LocalTime.of(12, 0));
        roomData.setEndTime(LocalTime.of(23, 0));
        Room room = roomService.register(user1, place1.getId(), roomData);

        RoomData roomUpdate = new RoomData();
        roomUpdate.setName("방2");
        roomUpdate.setDescription("작은 방");
        roomUpdate.setCapacity(2);
        roomUpdate.setBookType(BookType.TIME);
        roomUpdate.setStartTime(LocalTime.of(12, 0));
        roomUpdate.setEndTime(LocalTime.of(23, 30));
        roomService.update(user1, room.getId(), roomUpdate);

        assertThat(room.getName()).isEqualTo("방2");
        assertThat(room.getDescription()).isEqualTo("작은 방");
        assertThat(room.getCapacity()).isEqualTo(2);
        TimeBooking booking = (TimeBooking) room.getBooking();
        assertThat(booking.getStartTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(booking.getEndTime()).isEqualTo(LocalTime.of(23, 30));
    }

    @Test
    @DisplayName("등록한 방을 수정할 때 예약방식을 변경 가능하다 TIME -> DAY")
    void test6_1() {
        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.TIME);
        roomData.setStartTime(LocalTime.of(12, 0));
        roomData.setEndTime(LocalTime.of(23, 0));
        Room room = roomService.register(user1, place1.getId(), roomData);

        RoomData roomUpdate = new RoomData();
        roomUpdate.setName("방2");
        roomUpdate.setDescription("작은 방");
        roomUpdate.setCapacity(2);
        roomUpdate.setBookType(BookType.DAY);
        roomUpdate.setCheckIn(LocalTime.of(12, 0));
        roomUpdate.setCheckOut(LocalTime.of(23, 30));
        roomService.update(user1, room.getId(), roomUpdate);

        assertThat(room.getName()).isEqualTo("방2");
        assertThat(room.getDescription()).isEqualTo("작은 방");
        assertThat(room.getCapacity()).isEqualTo(2);

        DayBooking booking = (DayBooking) room.getBooking();
        assertThat(booking.getCheckIn()).isEqualTo(LocalTime.of(12, 0));
        assertThat(booking.getCheckOut()).isEqualTo(LocalTime.of(23, 30));
    }

    @Test
    @DisplayName("등록한 방을 수정할 때 예약방식을 변경 가능하다 DAY -> TIME")
    void test6_2() {
        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.DAY);
        roomData.setCheckIn(LocalTime.of(12, 0));
        roomData.setCheckOut(LocalTime.of(23, 0));
        Room room = roomService.register(user1, place1.getId(), roomData);

        RoomData roomUpdate = new RoomData();
        roomUpdate.setName("방2");
        roomUpdate.setDescription("작은 방");
        roomUpdate.setCapacity(2);
        roomUpdate.setBookType(BookType.TIME);
        roomUpdate.setStartTime(LocalTime.of(12, 0));
        roomUpdate.setEndTime(LocalTime.of(23, 30));
        roomService.update(user1, room.getId(), roomUpdate);

        assertThat(room.getName()).isEqualTo("방2");
        assertThat(room.getDescription()).isEqualTo("작은 방");
        assertThat(room.getCapacity()).isEqualTo(2);

        TimeBooking booking = (TimeBooking) room.getBooking();
        assertThat(booking.getStartTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(booking.getEndTime()).isEqualTo(LocalTime.of(23, 30));
    }

    @Test
    @DisplayName("등록한 방을 삭제할 수 있다, 삭제 시 Booking 정보도 함께 삭제된다")
    void test7() {
        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.TIME);
        roomData.setStartTime(LocalTime.of(12, 0));
        roomData.setEndTime(LocalTime.of(23, 0));
        Room room = roomService.register(user1, place1.getId(), roomData);

        roomService.delete(user1, room.getId());

        Optional<Room> getRoom = roomRepository.findById(room.getId());
        assertThat(getRoom).isEmpty();
    }
}