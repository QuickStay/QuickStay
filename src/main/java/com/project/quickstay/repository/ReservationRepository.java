package com.project.quickstay.repository;

import com.project.quickstay.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT R FROM Reservation R WHERE R.room.id=:roomId")
    List<Reservation> findReservations(@Param("roomId") Long roomId);

}
