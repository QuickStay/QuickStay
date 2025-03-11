package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.reservation.entity.DayReservationRegister;
import com.project.quickstay.domain.reservation.dto.MyDayReservation;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.DayReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final DayReservationService dayReservationService;

    @GetMapping("/calendar/day/{roomId}")
    public String getDisabledDates(@PathVariable Long roomId, Model model) {
        List<LocalDate> reservedDates = dayReservationService.getReservedDate(roomId);
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
        dayReservationService.registerReservation(user, roomId, dayReservationRegister);

        return "redirect:/home";
    }

    @GetMapping("/reservation/list")
    public String reservationList(@Login User user, Model model) {
        List<MyDayReservation> myReservations = dayReservationService.getUserReservations(user.getId());
        log.info("myReservations={}", myReservations);
        model.addAttribute("myReservations", myReservations);
        return "myPage/myReservation";
    }

    @GetMapping("/reservation/list/{reservationId}")
    public String findReservations(User user, @PathVariable Long reservationId, Model model) {

        MyDayReservation reservation = new MyDayReservation();
        reservation = dayReservationService.getSpecificReservation(reservationId);
        model.addAttribute("reservation", reservation);
        return "/myPage/myReservationInfo";
    }

    @GetMapping("/reservation/cancel/{reservationId}")
    public String cancelReservation(@PathVariable Long reservationId) {
        dayReservationService.cancelReservation(reservationId);
        return "redirect:/reservation/list";
    }
}
