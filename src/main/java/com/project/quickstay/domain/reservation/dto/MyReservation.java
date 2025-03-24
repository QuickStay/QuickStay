package com.project.quickstay.domain.reservation.dto;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class MyReservation {

    //startTime or endTime이 null이라면 DayReservation, else TimeReservation
    private Long id;
    private Long roomId;

    private String roomName;
    private String address;
    private String contact;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private State state;

    public static List<MyReservation> of(List<Reservation> reservations) {
        return reservations.stream().map(MyReservation::new).toList();
    }

    public MyReservation(Reservation reservation) {
        this.id = reservation.getId();
        this.roomId = reservation.getRoom().getId();
        this.roomName = reservation.getRoom().getName();
        this.address = reservation.getRoom().getPlace().getAddress();
        this.contact = reservation.getRoom().getPlace().getContact();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.state = reservation.getState();
    }
}
