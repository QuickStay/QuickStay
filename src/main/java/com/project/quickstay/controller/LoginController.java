package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.user.dto.SelectUserType;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.LoginService;
import com.project.quickstay.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;

    @Value("${spring.kakao.client_id}")
    String clientId;

    @Value("${spring.kakao.redirect_uri}")
    String redirectUri;

    @GetMapping("/oauth/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId
                + "&redirect_uri=" + redirectUri;

        response.sendRedirect(url);
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/callback/kakao")
    public String kakaoLogin(@RequestParam String code, final HttpServletRequest request) {
        User loginUser = loginService.kakaoService(code);
        HttpSession session = request.getSession();

        if (loginUser != null) {
            session.setAttribute("loginUser", loginUser);
            session.setMaxInactiveInterval(60 * 60); // 3600초 : 1시간
        }

        String redirectURL = "/home";

        return "redirect:" + redirectURL;
    }

    @GetMapping("/newUser")
    public String selectUserTypeForm(Model model) {
        model.addAttribute("selectUserType", new SelectUserType());
        return "selectUserType";
    }

    @PostMapping("/selectUserType")
    public String selectUserType(HttpServletRequest request, @Login User user, SelectUserType selectUserType) {
        User inputUser = userService.inputUserType(user, selectUserType);
        request.getSession(false).invalidate();

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", inputUser);
        session.setMaxInactiveInterval(60 * 60);

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/home";
    }
}
