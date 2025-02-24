package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.common.Social;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.dto.PlaceSearch;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.repository.RoomRepository;
import com.project.quickstay.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PlaceServiceTest {

    @Autowired
    PlaceService placeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    User user1;

    @BeforeEach
    void setUser() {
        UserRegister userRegister = new UserRegister();
        userRegister.setEmail("tkddnr@naver.com");
        userRegister.setNickname("상욱");
        userRegister.setSocial(Social.KAKAO);
        User user = User.register(userRegister);
        user1 = userRepository.save(user);
    }

    @Test
    @DisplayName("사용자는 장소를 등록할 수 있다")
    void test1() {
        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setAddress("강원도 춘천시");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user1, placeRegister);

        assertThat(place.getId()).isNotNull();
        assertThat(place.getName()).isEqualTo("한옥");
        assertThat(place.getDescription()).isEqualTo("우아한 한옥입니다");
        assertThat(place.getAddress()).isEqualTo("강원도 춘천시");
        assertThat(place.getContact()).isEqualTo("01012345678");
    }

    @Test
    @DisplayName("사용자는 장소를 수정할 수 있다")
    void test2() {
        PlaceRegister placeRegister1 = new PlaceRegister();
        placeRegister1.setName("한옥");
        placeRegister1.setDescription("우아한 한옥입니다");
        placeRegister1.setAddress("강원도 춘천시");
        placeRegister1.setContact("01012345678");

        Place place = placeService.register(user1, placeRegister1);

        PlaceRegister placeRegister2 = new PlaceRegister();
        placeRegister2.setName("호텔");
        placeRegister2.setDescription("신축 호텔");
        placeRegister2.setAddress("강원도 춘천시");
        placeRegister2.setContact("01087654321");

        placeService.update(user1, place.getId(), placeRegister2);

        assertThat(place.getId()).isNotNull();
        assertThat(place.getName()).isEqualTo("호텔");
        assertThat(place.getDescription()).isEqualTo("신축 호텔");
        assertThat(place.getAddress()).isEqualTo("강원도 춘천시");
        assertThat(place.getContact()).isEqualTo("01087654321");
    }

    @Test
    @DisplayName("장소 삭제 - 1. 룸이 등록되지 않은 장소를 삭제할 수 있다")
    void test3() {
        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setAddress("강원도 춘천시");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user1, placeRegister);

        placeService.delete(user1, place.getId());

        Optional<Place> getPlace = placeRepository.findById(place.getId());
        assertThat(getPlace).isEmpty();
    }

    @Test
    @DisplayName("장소 삭제 - 2. 장소에 등록된 룸이 있으면 삭제되지 않는다")
    void test4() {
        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setAddress("강원도 춘천시");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user1, placeRegister);

        RoomData roomData = new RoomData();
        roomData.setName("방1");
        roomData.setDescription("넓은 방");
        roomData.setCapacity(4);

        roomData.setBookType(BookType.DAY);
        roomData.setCheckIn(LocalTime.of(15, 0));
        roomData.setCheckOut(LocalTime.of(11, 0));
        Room room = roomService.register(user1, place.getId(), roomData);

        assertThatThrownBy(() -> placeService.delete(user1, place.getId())).isInstanceOf(ServiceException.class);


    }

    @Test
    @DisplayName("장소 검색 - 1. 장소 이름 검색")
    void test5() {
        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setAddress("강원도 춘천시");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user1, placeRegister);

        Page<PlaceSearch> search = placeService.search("한", 1);
        assertThat(search.getNumberOfElements()).isEqualTo(1);
        assertThat(search.getContent().get(0).getName()).isEqualTo("한옥");
    }

    @Test
    @DisplayName("장소 검색 - 2. 장소 설명 검색")
    void test6() {
        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setAddress("강원도 춘천시");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user1, placeRegister);

        Page<PlaceSearch> search = placeService.search("우아한", 1);
        assertThat(search.getNumberOfElements()).isEqualTo(1);
        assertThat(search.getContent().get(0).getName()).isEqualTo("한옥");
    }

    @Test
    @DisplayName("장소 검색 - 2. 장소 주소 검색")
    void test7() {
        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setAddress("강원도 춘천시");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user1, placeRegister);

        Page<PlaceSearch> search = placeService.search("춘천", 1);
        assertThat(search.getNumberOfElements()).isEqualTo(1);
        assertThat(search.getContent().get(0).getName()).isEqualTo("한옥");
    }
}
