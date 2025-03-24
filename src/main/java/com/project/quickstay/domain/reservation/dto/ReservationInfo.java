package com.project.quickstay.domain.reservation.dto;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationInfo {

    private Long id;

    private String placeName;
    private String placeDescription;
    private int capacity;

    private State state;

    private String roomName;
    private String roomDescription;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;


    public static ReservationInfo of(Reservation reservation) {
        ReservationInfo reservationInfo = new ReservationInfo();
        reservationInfo.id = reservation.getId();
        reservationInfo.placeName = reservation.getRoom().getPlace().getName();
        reservationInfo.placeDescription = reservation.getRoom().getPlace().getDescription();
        reservationInfo.capacity = reservation.getRoom().getCapacity();

        reservationInfo.state = reservation.getState();

        reservationInfo.roomName = reservation.getRoom().getName();
        reservationInfo.roomDescription = reservation.getRoom().getDescription();

        reservationInfo.startDate = reservation.getStartDate();
        reservationInfo.endDate = reservation.getStartDate();
        reservationInfo.startTime = reservation.getStartTime();
        reservationInfo.endTime = reservation.getEndTime();
        return reservationInfo;
    }

}
