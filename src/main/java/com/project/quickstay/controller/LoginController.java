package com.project.quickstay.controller;

import com.project.quickstay.common.KakaoProvider;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

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
    public String kakaoLogin(@RequestParam String code, final HttpServletRequest request) throws IOException {
        User loginUser = loginService.kakaoService(code);
        HttpSession session = request.getSession();
        if (loginUser != null) {
            session.setAttribute("loginUser", loginUser);
            session.setMaxInactiveInterval(60 * 60); // 3600초 : 1시간
        }

        // redirect 주소
        String redirectURL = "/home";

        return "redirect:"+redirectURL;
    }

    @GetMapping("/logout/kakao")
    public String logout(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // session 속 모든 속성값 삭제
        }

        return "redirect:/login/kakao";
    }
}
