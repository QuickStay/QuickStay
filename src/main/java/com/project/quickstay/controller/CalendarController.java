package com.project.quickstay.controller;

import com.project.quickstay.service.DayReservationService;
import com.project.quickstay.service.TimeReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

    private final TimeReservationService timeReservationService;

    @GetMapping("/calendar/time/{roomId}")
    public String getDisabledTimes(@PathVariable Long roomId, Model model) {
        List<LocalTime> reservedTimes = timeReservationService.getReservedTime(roomId);
        model.addAttribute("disabledTimes", reservedTimes);
        return "reservation/timeReservationList";
    }


}