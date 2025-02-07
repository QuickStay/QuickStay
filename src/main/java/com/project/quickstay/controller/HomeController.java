package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(@Login User user) {
        log.info("user id = {}, nickname = {}, email = {}"
                , user.getId(), user.getNickname(), user.getEmail());
        return "main";
    }
}
