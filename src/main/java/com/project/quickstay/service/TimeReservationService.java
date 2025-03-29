package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeReservationService implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public List<LocalDateTime> getReserved(Long roomId) {
        List<Reservation> reservations = getReservedReservation(roomId);
        List<LocalDateTime> times = new ArrayList<>();
        for (Reservation reservation : reservations) {
            LocalTime time = reservation.getStartTime();
            LocalDate date = reservation.getStartDate();
            while (!time.equals(reservation.getEndTime())) {
                times.add(LocalDateTime.of(date, time));
                time = time.plusMinutes(30);
            }
        }
        return times;
    }

    @Override
    public boolean supports(BookType bookType) {
        return BookType.TIME == bookType;
    }

    private List<Reservation> getReservedReservation(Long roomId) {
        return reservationRepository.findReserved(roomId, LocalDate.now());
    }
}
