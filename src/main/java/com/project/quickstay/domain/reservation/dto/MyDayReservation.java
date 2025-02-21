package com.project.quickstay.domain.reservation.dto;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.room.entity.Room;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MyDayReservation {
    // 방 이름
    // 예약 기간
    // 장소 위치
    // 연락처
    // 상태
    private Long id;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;
    private State state;
}
