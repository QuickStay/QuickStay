package com.project.quickstay.equalsTest;

import com.project.quickstay.common.Social;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.repository.UserRepository;
import com.project.quickstay.service.PlaceService;
import jakarta.persistence.EntityManager;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LazyLoadingEqualsTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    EntityManager em;

    /**
     * @Override
     * public boolean equals(Object o) {
     *     if (this == o) return true;
     *     if (o == null || getClass() != o.getClass()) return false;
     *
     *     User user = (User) o;
     *
     *     return Objects.equals(id, user.id);
     * }
     */
    @Test
    @DisplayName("ide의 equals 사용")
    void test1() {
        UserRegister userRegister = new UserRegister();
        userRegister.setEmail("tkddnr@naver.com");
        userRegister.setNickname("상욱");
        userRegister.setSocial(Social.KAKAO);
        User user = User.register(userRegister);
        user = userRepository.save(user);

        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setProvince("강원도");
        placeRegister.setCity("춘천시");
        placeRegister.setDetailAddress("강원대학교");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user, placeRegister);

        em.flush();
        em.clear();

        Place getPlace1 = placeRepository.findById(place.getId()).get();

        //프록시 객체인지 확인
        User proxyUser = getPlace1.getUser();
        assertThat(proxyUser).isInstanceOf(HibernateProxy.class);
        //이메일을 가져오면서 쿼리 날려 데이터 가져오기
        String email = proxyUser.getEmail();
        assertThat(proxyUser.getEmail()).isEqualTo(email);

        em.flush();
        em.clear();

        User nonProxyUser = userRepository.findById(user.getId()).get();
        //프록시 객체가 아닌것을 확인
        assertThat(nonProxyUser).isNotInstanceOf(HibernateProxy.class);

        System.out.println(nonProxyUser.equals(proxyUser)); //false
        System.out.println(proxyUser.equals(nonProxyUser)); //true

    }

    /**
     * @Override
     * public boolean equals(Object o) {
     *     if (this == o) return true;
     *     if (!(o instanceof User user)) {
     *         return false;
     *     }
     *
     *     return Objects.equals(id, user.getId());
     * }
     */
    @Test
    @DisplayName("커스텀한 equals 사용")
    void test2() {
        UserRegister userRegister = new UserRegister();
        userRegister.setEmail("tkddnr@naver.com");
        userRegister.setNickname("상욱");
        userRegister.setSocial(Social.KAKAO);
        User user = User.register(userRegister);
        user = userRepository.save(user);

        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setProvince("강원도");
        placeRegister.setCity("춘천시");
        placeRegister.setDetailAddress("강원대학교");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user, placeRegister);

        em.flush();
        em.clear();

        Place getPlace1 = placeRepository.findById(place.getId()).get();

        //프록시 객체인지 확인
        User proxyUser = getPlace1.getUser();
        assertThat(proxyUser).isInstanceOf(HibernateProxy.class);
        //이메일을 가져오면서 쿼리 날려 데이터 가져오기
        String email = proxyUser.getEmail();
        assertThat(proxyUser.getEmail()).isEqualTo(email);

        em.flush();
        em.clear();

        User nonProxyUser = userRepository.findById(user.getId()).get();
        //프록시 객체가 아닌것을 확인
        assertThat(nonProxyUser).isNotInstanceOf(HibernateProxy.class);

        System.out.println(nonProxyUser.equals(proxyUser)); //true
        System.out.println(proxyUser.equals(nonProxyUser)); //true

    }

    /**
     * @EqualsAndHashCode(onlyExplicitlyIncluded = true)
     * public class User {
     *
     * @Id
     * @GeneratedValue(strategy = GenerationType.IDENTITY)
     * @EqualsAndHashCode.Include
     * private Long id;
     */
    @Test
    @DisplayName("lombok에서 제공하는 equals 사용")
    void test3() {
        UserRegister userRegister = new UserRegister();
        userRegister.setEmail("tkddnr@naver.com");
        userRegister.setNickname("상욱");
        userRegister.setSocial(Social.KAKAO);
        User user = User.register(userRegister);
        user = userRepository.save(user);

        PlaceRegister placeRegister = new PlaceRegister();
        placeRegister.setName("한옥");
        placeRegister.setDescription("우아한 한옥입니다");
        placeRegister.setProvince("강원도");
        placeRegister.setCity("춘천시");
        placeRegister.setDetailAddress("강원대학교");
        placeRegister.setContact("01012345678");

        Place place = placeService.register(user, placeRegister);

        em.flush();
        em.clear();

        Place getPlace1 = placeRepository.findById(place.getId()).get();

        //프록시 객체인지 확인
        User proxyUser = getPlace1.getUser();
        assertThat(proxyUser).isInstanceOf(HibernateProxy.class);
        //이메일을 가져오면서 쿼리 날려 데이터 가져오기
        String email = proxyUser.getEmail();
        assertThat(proxyUser.getEmail()).isEqualTo(email);

        em.flush();
        em.clear();

        User nonProxyUser = userRepository.findById(user.getId()).get();
        //프록시 객체가 아닌것을 확인
        assertThat(nonProxyUser).isNotInstanceOf(HibernateProxy.class);

        System.out.println(nonProxyUser.equals(proxyUser)); //true
        System.out.println(proxyUser.equals(nonProxyUser)); //true

    }
}
