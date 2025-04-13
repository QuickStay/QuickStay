package com.project.quickstay.common.batch;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.reservation.entity.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
public class ReservationItemProcessor implements ItemProcessor<Reservation, Reservation> {

    @Override
    public Reservation process(Reservation reservation) {
        LocalDate nowDate = LocalDate.now();
        if (reservation.getEndDate().isBefore(nowDate)) {
            reservation.updateState(State.COMPLETED);
            return reservation;
        }

        LocalTime nowTime = LocalTime.now();
        if (reservation.getEndTime() != null) {
            if (reservation.getEndDate().isEqual(nowDate) && reservation.getEndTime().isBefore(nowTime)) {
                reservation.updateState(State.COMPLETED);
            }
        }

        return reservation;
    }
}
