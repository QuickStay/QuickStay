package com.project.quickstay.service;

import com.project.quickstay.common.Social;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PlaceServiceTest {

    @Autowired
    PlaceService placeService;

    @Autowired
    UserRepository userRepository;

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
}
