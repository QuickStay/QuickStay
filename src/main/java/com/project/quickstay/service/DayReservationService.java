package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.dto.MyReservation;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DayReservationService implements ReservationService{

    private final ReservationRepository reservationRepository;

    @Override
    public List<LocalDate> getReserved(Long roomId) {
        List<Reservation> reservations = getReservedReservation(roomId);
        List<LocalDate> dates = new ArrayList<>();
        for (Reservation reservation : reservations) {
            LocalDate date = reservation.getStartDate();
            while (!date.isEqual(reservation.getEndDate())) {
                dates.add(date);
                date = date.plusDays(1);
            }
        }
        return dates;
    }

    @Override
    public boolean supports(BookType bookType) {
        return bookType == BookType.DAY;
    }

    private List<Reservation> getReservedReservation(Long roomId) {
        return reservationRepository.findReserved(roomId, LocalDate.now());
    }
}
