package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.dto.TimeReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
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
    // 1. 어느 Place-room의 모든 Reservation table 가져오기
    // 2. 전처리
    public Reservation registerTimeReservation(User user, Long roomId, TimeReservationRegister timeReservationRegister) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()) {
            throw new IllegalStateException("Room not found");
        }
        Reservation newReservation = Reservation.timeRegister(user, room.get(), timeReservationRegister);
        return reservationRepository.save(newReservation);
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
