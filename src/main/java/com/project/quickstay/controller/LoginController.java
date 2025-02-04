package com.project.quickstay.controller;

import com.project.quickstay.domain.KakaoProvider;
import com.project.quickstay.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final KakaoProvider kakaoProvider;
    private final LoginService loginService;

    @Value("${spring.kakao.client_id}")
    String clientId;

    @GetMapping("/login/kakao")
    public String login() {
        return "login";
    }

    @GetMapping("/oauth/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=http://localhost:8080/api/oauth/kakao";
        response.sendRedirect(url);
    }

    @GetMapping("/api/oauth/kakao")
    public String kakaoLogin(@RequestParam String code) throws IOException {
        loginService.kakaoService(code);

        return "main";
    }
}
