package com.project.quickstay.repository;

import com.project.quickstay.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT R FROM Reservation R WHERE R.room.id=:roomId AND R.startDate >:now AND R.state = com.project.quickstay.common.State.RESERVED")
    List<Reservation> findReserved(@Param("roomId") Long roomId, @Param("now") LocalDate localDate);

}
