package com.project.quickstay.service;

import com.project.quickstay.common.State;
import com.project.quickstay.common.eventListener.EventHandler;
import com.project.quickstay.common.eventListener.ReservationEvent;
import com.project.quickstay.domain.reservation.dto.MyReservation;
import com.project.quickstay.domain.reservation.dto.OperatingHours;
import com.project.quickstay.domain.reservation.dto.ReservationInfo;
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
    private final EventHandler eventHandler;

    public List<? extends Temporal> getReserved(Long roomId) {
        Room room = getRoomById(roomId);
        ReservationService service = selector.getService(room);
        return service.getReserved(roomId);
    }

    //공통 로직은 handler에서 처리
    public OperatingHours getOperatingHours(Long roomId) {
        Room room = getRoomById(roomId);
        return room.getOperatingHours();
    }

    public Reservation reservationRegister(User user, Long roomId, ReservationRegister register) {
        Room room = getRoomById(roomId);
        register.placeRoom(room);
        register.placeUser(user);
        Reservation reservation = register.createReservation();
        eventHandler.handleReservationRegister(new ReservationEvent(room));
        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId, User user) {
        validUser(reservationId, user);
        Reservation reservation = getReservationById(reservationId);
        reservation.updateState(State.CANCELLED);
        eventHandler.handleReservationCancel(new ReservationEvent(reservation.getRoom()));
    }

    public List<MyReservation> getUserReservations(Long userId) {
        List<Reservation> allReservations = reservationRepository.findAllByUserId(userId);
        return MyReservation.of(allReservations);
    }

    public ReservationInfo getSpecificReservation(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        return ReservationInfo.of(reservation);
    }

    private Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new ServiceException("방이 없습니다."));
    }

    private Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new ServiceException("예약이 없습니다."));
    }

    private void validUser(Long reservationId, User user) {
        Reservation reservation = getReservationById(reservationId);
        if (!reservation.getUser().equals(user)) {
            throw new ServiceException("권한이 없습니다.");
        }
    }
}
