package com.project.quickstay.service;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.reservation.dto.DayReservationRegister;
import com.project.quickstay.domain.reservation.dto.MyDayReservation;
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
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DayReservationService {

    private final RoomRepository roomRepository;
    // 1. 어느 Place-room의 모든 Reservation table 가져오기
    // 2. 전처리
    private final ReservationRepository reservationRepository;

    public Reservation registerDayReservation(User user, Long roomId, DayReservationRegister dayReservationRegister) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()) {
            throw new ServiceException("방이 없습니다.");
        }
        Reservation newReservation = Reservation.dayRegister(user, room.get(), dayReservationRegister);
        return reservationRepository.save(newReservation);
    }

    private List<Reservation> getReserved(Long roomId) {
        return reservationRepository.findReserved(roomId, LocalDate.now());
    }

    public List<LocalDate> getReservedDate(Long roomId) {
        List<Reservation> reservations = getReserved(roomId);
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

    public List<MyDayReservation> getUserReservations(Long userId) {
        List<Reservation> allReservations = reservationRepository.findAllByUserId(userId);
        List<MyDayReservation> myDayReservations = new ArrayList<>();

        for (Reservation reservation : allReservations) {
            MyDayReservation myReservation = new MyDayReservation();
            myReservation.setId(reservation.getId());
            myReservation.setRoom(reservation.getRoom());
            myReservation.setStartDate(reservation.getStartDate());
            myReservation.setEndDate(reservation.getEndDate());
            myReservation.setState(reservation.getState());
            myDayReservations.add(myReservation);
        }

        return myDayReservations;
    }

    public MyDayReservation getSpecificReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        MyDayReservation myReservation = new MyDayReservation();
        myReservation.setId(reservationId);
        myReservation.setRoom(reservation.get().getRoom());
        myReservation.setStartDate(reservation.get().getStartDate());
        myReservation.setEndDate(reservation.get().getEndDate());
        myReservation.setState(reservation.get().getState());
        return myReservation;
    }

    public void cancelReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        reservation.get().updateState(State.CANCELLED);
    }
}
