package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.reservation.entity.ReservationRegister;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.user.entity.User;

import java.time.temporal.Temporal;
import java.util.List;


public interface ReservationService {

    boolean supports(BookType bookType);

    List<? extends Temporal> getReserved(Long roomId);

    Reservation reservationRegister(User user, Long roomId, ReservationRegister register);
}
