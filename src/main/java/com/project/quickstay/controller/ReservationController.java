package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.reservation.dto.DayArticleForm;
import com.project.quickstay.domain.reservation.dto.DayReservationRegister;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.DayReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final DayReservationService dayReservationService;

    @PostMapping("/reserve/day/{roomId}")
    public String reserveDay(@PathVariable Long roomId, @Login User user, DayArticleForm dayArticleForm) {
        log.info("startDate={}, endDate={}", dayArticleForm.getStartDate(), dayArticleForm.getEndDate());
        // 예약 처리 시키고 뷰 보내기
        DayReservationRegister dayReservationRegister = new DayReservationRegister();
        dayReservationRegister.setStartDate(dayArticleForm.getStartDate());
        dayReservationRegister.setEndDate(dayArticleForm.getEndDate());
        dayReservationService.registerDayReservation(user, roomId, dayReservationRegister);
        return "/reservation/reservationForm";
    }


}
