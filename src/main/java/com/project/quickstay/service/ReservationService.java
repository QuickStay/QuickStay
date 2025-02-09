package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.dto.DayReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.repository.ReservationRepository;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final RoomRepository roomRepository;
    // 1. 어느 Place-room의 모든 Reservation table 가져오기
    // 2. 전처리
    private final ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations(Long roomId) {
        return reservationRepository.findReservations(roomId);
    }

    public Reservation registerDayReservation(User user, Long roomId, DayReservationRegister dayReservationRegister) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()) {
            throw new IllegalStateException("Room not found");
        }
        Reservation newReservation = Reservation.dayRegister(user, room.get(), dayReservationRegister);
        return reservationRepository.save(newReservation);
    }
}
