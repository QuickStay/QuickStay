package com.project.quickstay.common.interceptor;

import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.domain.user.entity.UserType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class NewUserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loginUser");

        if (user.getUserType() == UserType.NEW_USER) {
            response.sendRedirect("/newUser");
            return false;
        }
        return true;
    }
}
