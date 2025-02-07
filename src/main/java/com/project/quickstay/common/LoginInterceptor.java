package com.project.quickstay.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    private static final String loginUser = "loginUser";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String redirectURI = "/login/kakao";
        HttpSession session = request.getSession();
        Object obj = session.getAttribute(loginUser);

        if (obj == null) {
            log.info("로그인 하지 않음");
            response.sendRedirect(redirectURI);
            return false;
        }
        return true;
    }
}
