package com.project.quickstay.controller;

import com.project.quickstay.common.BookType;
import com.project.quickstay.common.Social;
import com.project.quickstay.domain.place.dto.PlaceRegister;
import com.project.quickstay.domain.room.dto.RoomRegister;
import com.project.quickstay.repository.UserRepository;
import com.project.quickstay.service.PlaceService;
import com.project.quickstay.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
@Slf4j
public class PlaceController {

    private final PlaceService placeService;
    private final RoomService roomService;
    private final UserRepository userRepository;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("placeRegister", new PlaceRegister());
        return "place/placeRegister";
    }

    @PostMapping("/register")
    public String register(@Valid PlaceRegister placeRegister, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("bindingResult = {}", bindingResult);
            return "place/placeRegister";
        }
        placeService.register(userRepository.findBySocialAndEmail(Social.KAKAO, "thstkddnr20@naver.com").get(), placeRegister);
        return "main";
    }

    @GetMapping("/{placeId}/register")
    public String roomRegisterForm(@PathVariable Long placeId, Model model) {
        model.addAttribute("roomRegister", new RoomRegister());
        model.addAttribute("placeId", placeId);
        return "place/room/roomRegister";
    }

    @PostMapping("/{placeId}/register")
    public String register(@PathVariable Long placeId, @Valid RoomRegister roomRegister, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("bindingResult = {}", bindingResult);
            return "place/room/roomRegister";
        }

        if (roomRegister.getBookType() == BookType.TIME) {
            LocalTime startTime = roomRegister.getStartTime();
            LocalTime endTime = roomRegister.getEndTime();
            if (endTime.isBefore(startTime) || endTime.equals(startTime) || startTime.plusHours(1).isAfter(endTime)) {
                /**
                 * 1. endTime > startTime
                 * 2. endTime = startTime
                 * 3. startTime + 1hour > endTime //ex) startTime 7:00, endTime 7:30
                 */
                bindingResult.rejectValue("endTime", "Error.time");
                return "place/room/roomRegister";
            }
        }
        roomService.register(placeId, roomRegister);
        return "main";

    }
}
