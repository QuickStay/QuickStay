package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.reservation.dto.MyReservation;
import com.project.quickstay.domain.reservation.dto.ReservationInfo;
import com.project.quickstay.domain.reservation.entity.DayReservationRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.ReservationHandler;
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

    private final ReservationHandler reservationHandler;

    @GetMapping("/calendar/day/{roomId}")
    public String getDisabledDates(@PathVariable Long roomId, Model model) {
        List<?> reservedDates = reservationHandler.getReserved(roomId);
        model.addAttribute("disabledDates", reservedDates);
        return "reservation/dayReservationList";
    }

    @PostMapping("/reservation/day/{roomId}")
    public String reserveDay(@PathVariable Long roomId, DayReservationRegister dayReservationRegister, Model model) {
        model.addAttribute("dayReservationRegister", dayReservationRegister);
        return "reservation/reservationForm";
    }

    @PostMapping("/reservation/day/{roomId}/confirm")
    public String reserveDayConfirm(@PathVariable Long roomId, @Login User user, DayReservationRegister dayReservationRegister) {
        reservationHandler.reservationRegister(user, roomId, dayReservationRegister);
        return "redirect:/home";
    }


    /**
     * TODO Time 예약
     * 1. 예약 가능 날짜 고르기 /calendar/time/{roomId}
     * 2. 해당 날짜에 대한 시간 고르기 /calendar/time/{roomId}/{date}
     * 3. 예약 진행 /reservation/time/{roomId}
     * 4. 예약 확정 /reservation/time/{roomId}/confirm
     */

    @GetMapping("/calendar/time/{roomId}")
    public String getDisabledTimes(@PathVariable Long roomId, Model model) {
        List<?> reservedTimes = reservationHandler.getReserved(roomId);
        model.addAttribute("disabledTimes", reservedTimes);
        return "reservation/timeReservationList";
    }

    @GetMapping("/reservation/list")
    public String reservationList(@Login User user, Model model) {
        List<MyReservation> myReservations = reservationHandler.getUserReservations(user.getId());
        model.addAttribute("myReservations", myReservations);
        return "myPage/myReservation";
    }

    @GetMapping("/reservation/{reservationId}")
    public String findReservations(@Login User user, @PathVariable Long reservationId, Model model) {
        ReservationInfo reservation = reservationHandler.getSpecificReservation(reservationId, user);
        model.addAttribute("reservation", reservation);
        return "/myPage/myReservationInfo";
    }

    //예약 취소 - 공통 처리
    @GetMapping("/reservation/cancel/{reservationId}")
    public String cancelReservation(@PathVariable Long reservationId) {
        reservationHandler.cancelReservation(reservationId);
        return "redirect:/reservation/list";
    }

}
