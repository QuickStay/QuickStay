package com.project.quickstay.controller;

import com.project.quickstay.service.DayReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CalendarController {
    DayReservationService dayReservationService;

    @GetMapping("/calendar")
    public String calendar() {
        return "reservationList";
    }

    @GetMapping("/calendar/day/{roomId}/")
    public String getDisabledDates(@PathVariable Long roomId, Model model) {
        List<LocalDate> reservedDates = dayReservationService.getReservedDate(roomId);
        model.addAttribute("disabledDates", reservedDates);
        return "reservationList";
    }
}