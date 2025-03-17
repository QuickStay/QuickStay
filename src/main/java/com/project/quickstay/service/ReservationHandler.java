package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.reservation.entity.ReservationRegister;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.ReservationRepository;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationHandler {

    private final ReservationServiceSelector selector;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public List<? extends Temporal> getReserved(Long roomId) {
        Room room = getRoomById(roomId);
        ReservationService service = selector.getService(room);
        return service.getReserved(roomId);
    }

    //공통 로직은 handler에서 처리
    public Reservation reservationRegister(User user, Long roomId, ReservationRegister register) {
        Room room = getRoomById(roomId);
        register.placeRoom(room);
        register.placeUser(user);
        Reservation reservation = register.createReservation();
        return reservationRepository.save(reservation);
    }

    private Room getRoomById(Long roomId) {
        Optional<Room> getRoom = roomRepository.findById(roomId);
        if (getRoom.isEmpty()) {
            throw new ServiceException("방이 없습니다.");
        }
        return getRoom.get();
    }
}
