package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.entity.ReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.ReservationRepository;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeReservationService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public Reservation registerTimeReservation(User user, Long roomId, ReservationRegister reservationRegister) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()) {
            throw new ServiceException("방이 없습니다.");
        }
        Reservation reservation = reservationRegister.createReservation();
        return reservationRepository.save(reservation);
    }

    private List<Reservation> getReserved(Long roomId) {
        return reservationRepository.findReserved(roomId, LocalDate.now());
    }

    public List<LocalTime> getReservedTime(Long roomId) {
        List<Reservation> reservations = getReserved(roomId);
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
}
