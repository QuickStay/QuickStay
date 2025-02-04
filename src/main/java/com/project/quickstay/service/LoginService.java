package com.project.quickstay.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.quickstay.common.Social;
import com.project.quickstay.domain.KakaoProvider;
import com.project.quickstay.domain.user.dto.UserRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoginService {
    private final KakaoProvider kakaoProvider;
    private final UserRepository userRepository;

    public User kakaoService(String code) {
        // 토큰 받기
        String accessToken = kakaoProvider.getAccessToken(code);
        // 사용자 정보 받기
        String userInfo = kakaoProvider.getKakaoInfo(accessToken);
        log.info("accessToken: {}, userInfo: {}", accessToken, userInfo);

        // 1. email, nickname : 스트링
        JsonObject info = JsonParser.parseString(userInfo).getAsJsonObject();

        String nickname = info.getAsJsonObject("properties").get("nickname").getAsString();
        String email = info.getAsJsonObject("kakao_account").get("email").getAsString();
        log.info("email: {}, nickname: {}", email, nickname);

        // 2. userEntity에 social, email
        Optional<User> getUser = userRepository.findBySocialAndEmail(Social.KAKAO, email);

        if (getUser.isEmpty()) {
            UserRegister userRegister = new UserRegister();
            userRegister.setNickname(nickname);
            userRegister.setEmail(email);
            userRegister.setSocial(Social.KAKAO);

            User newUser = User.register(userRegister);
            log.info("user: {}", newUser);
            return userRepository.save(newUser);
        }
        log.info("user: {}", getUser.get());
        return getUser.get();
    }
}
