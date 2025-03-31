package com.project.quickstay.eventListener;

import com.project.quickstay.common.eventListener.ReservationEvent;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.reservation.entity.ReservationRegister;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.ReservationRepository;
import com.project.quickstay.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class FakeReservationHandler {

    @Autowired
    FakeEventHandler eventHandler;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RoomRepository roomRepository;

    public Reservation reservationRegister1_throwsException(User user, Long roomId, ReservationRegister register) {
        Room room = getRoomById(roomId);
        register.placeRoom(room);
        register.placeUser(user);
        Reservation reservation = register.createReservation();
        throwException();
        eventHandler.handle_T_After_Commit(new ReservationEvent(room));
        return reservation;
    }

    public Reservation reservationRegister2_normalOperation(User user, Long roomId, ReservationRegister register) {
        Room room = getRoomById(roomId);
        register.placeRoom(room);
        register.placeUser(user);
        Reservation reservation = register.createReservation();
        eventHandler.handle_T_After_Commit(new ReservationEvent(room));
        return reservationRepository.save(reservation);
    }

    private Room getRoomById(Long roomId) {
        Optional<Room> getRoom = roomRepository.findById(roomId);
        if (getRoom.isEmpty()) {
            throw new ServiceException("방이 없습니다.");
        }
        return getRoom.get();
    }

    private void throwException() {
        throw new ServiceException("실패!");
    }
}
