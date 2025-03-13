package com.project.quickstay.service;

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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeReservationService implements ReservationService {

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
