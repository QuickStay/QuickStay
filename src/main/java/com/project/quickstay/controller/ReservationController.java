package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.reservation.dto.DayReservationRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.DayReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final DayReservationService dayReservationService;

    @PostMapping("/reservation/day/{roomId}")
    public String reserveDay(@PathVariable Long roomId, DayReservationRegister dayReservationRegister, Model model) {
        log.info("startDate={}, endDate={}", dayReservationRegister.getStartDate(), dayReservationRegister.getEndDate());
        model.addAttribute("dayReservationRegister", dayReservationRegister);

        return "reservation/reservationForm";
    }

    @PostMapping("/reservation/day/{roomId}/confirm")
    public String reserveDay(@PathVariable Long roomId, @Login User user, DayReservationRegister dayReservationRegister) {
        dayReservationService.registerDayReservation(user, roomId, dayReservationRegister);

        return "redirect:/home";
    }

    @GetMapping("/reservation/list")
    public String reservationList() {

        return "myPage/myReservation";
    }
}
