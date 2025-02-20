package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.reservation.dto.DayReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.DayReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
    public String reservationList(@Login User user, Model model) {
        // model - reservation list 보내기
        List<Reservation> myReservations = dayReservationService.getUserReservations(user.getId());
        log.info("myReservations={}", myReservations);
        model.addAttribute("myReservations", myReservations);
        return "myPage/myReservation";
    }

//    @GetMapping("/reservation/list/{roomId}")
//    public String findReservations(User user) {
//        // 방 정보 & 예약 날짜 or 시간 정보
//        return "/myPage/myReservationInfo";
//    }
}
