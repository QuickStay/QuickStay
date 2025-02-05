package com.project.quickstay.common;

import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserInitializer {

    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initUser() {
        return args -> {
            if (userRepository.findBySocialAndEmail(Social.KAKAO, "thstkddnr20@naver.com").isEmpty()) {
                UserRegister userRegister = new UserRegister();
                userRegister.setEmail("thstkddnr20@naver.com");
                userRegister.setNickname("상욱");
                userRegister.setSocial(Social.KAKAO);

                User user = User.register(userRegister);
                userRepository.save(user);
            }
        };
    }

}
