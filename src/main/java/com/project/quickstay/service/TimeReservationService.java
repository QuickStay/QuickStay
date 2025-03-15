package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.repository.ReservationRepository;
import com.project.quickstay.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TimeReservationService extends ReservationService {

    private final ReservationRepository reservationRepository;

    public TimeReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        super(roomRepository, reservationRepository);
        this.reservationRepository = reservationRepository;
    }


    @Override
    public List<LocalTime> getReserved(Long roomId) {
        List<Reservation> reservations = getReservedReservation(roomId);
        List<LocalTime> times = new ArrayList<>();
        for (Reservation reservation : reservations) {
            LocalTime time = reservation.getStartTime();
            while (!time.equals(reservation.getEndTime())) {
                times.add(time);
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
