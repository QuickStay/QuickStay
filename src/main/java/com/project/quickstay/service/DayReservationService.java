package com.project.quickstay.service;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.reservation.dto.MyDayReservation;
import com.project.quickstay.domain.reservation.entity.ReservationRegister;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.room.entity.BookType;
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
public class DayReservationService implements ReservationService{

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation reservationRegister(User user, Long roomId, ReservationRegister reservationRegister) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()) {
            throw new ServiceException("방이 없습니다.");
        }
        reservationRegister.placeRoom(room.get());
        reservationRegister.placeUser(user);
        Reservation reservation = reservationRegister.createReservation();
        return reservationRepository.save(reservation);
    }

    @Override
    public List<LocalDate> getReserved(Long roomId) {
        List<Reservation> reservations = getReservedReservation(roomId);
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

    @Override
    public boolean supports(BookType bookType) {
        return bookType == BookType.DAY;
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

    private List<Reservation> getReservedReservation(Long roomId) {
        return reservationRepository.findReserved(roomId, LocalDate.now());
    }
}
