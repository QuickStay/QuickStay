package com.project.quickstay.batch;

import com.project.quickstay.CleanUp;
import com.project.quickstay.common.Social;
import com.project.quickstay.common.State;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.reservation.entity.DayReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.ReservationRepository;
import com.project.quickstay.repository.UserRepository;
import com.project.quickstay.service.PlaceService;
import com.project.quickstay.service.ReservationHandler;
import com.project.quickstay.service.RoomService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BatchJobLauncherTest {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job completeReservationJob;

    @Autowired
    CleanUp cleanUp;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    RoomService roomService;

    @Autowired
    ReservationHandler reservationHandler;

    @Autowired
    ReservationRepository reservationRepository;

    User user1;
    Place place1;
    Room room;
    Reservation reservation;

    @BeforeEach
    void set() {
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

        DayReservationRegister dayReservationRegister = new DayReservationRegister(user1, room);
        dayReservationRegister.setStartDate(LocalDate.of(2025, 2, 10));
        dayReservationRegister.setEndDate(LocalDate.of(2025, 2, 13));

        reservation = reservationHandler.reservationRegister(user1, room.getId(), dayReservationRegister);
    }

    @AfterEach
    void cleanUp() {
        cleanUp.execute();
    }

    @Test
    @DisplayName("Job 실행 확인")
    void test1() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(completeReservationJob, params);

        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        Reservation getReservation = reservationRepository.findById(reservation.getId()).get();
        assertThat(getReservation.getState()).isEqualTo(State.COMPLETED);

    }

    @Test
    @DisplayName("process 동작 방식 확인")
    void test2() throws Exception {

        //총 3개의 Reservation 생성 후 processor에 로그 남기도록하여 확인 - 확인하려면 process() 메서드에 로그 추가 필요
        DayReservationRegister day1 = new DayReservationRegister(user1, room);
        day1.setStartDate(LocalDate.of(2025, 2, 10));
        day1.setEndDate(LocalDate.of(2025, 2, 13));

        reservationHandler.reservationRegister(user1, room.getId(), day1);

        DayReservationRegister day2 = new DayReservationRegister(user1, room);
        day2.setStartDate(LocalDate.of(2025, 2, 10));
        day2.setEndDate(LocalDate.of(2025, 2, 13));

        reservationHandler.reservationRegister(user1, room.getId(), day2);


        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(completeReservationJob, params);

    }
}
