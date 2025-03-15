package com.project.quickstay.service;

import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.reservation.entity.ReservationRegister;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.ReservationRepository;
import com.project.quickstay.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public abstract class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public abstract boolean supports(BookType bookType);

    public abstract List<? extends Temporal> getReserved(Long roomId);

    public Reservation reservationRegister(User user, Long roomId, ReservationRegister register) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()) {
            throw new ServiceException("방이 없습니다.");
        }
        register.placeRoom(room.get());
        register.placeUser(user);
        Reservation reservation = register.createReservation();
        return reservationRepository.save(reservation);
    }
}
