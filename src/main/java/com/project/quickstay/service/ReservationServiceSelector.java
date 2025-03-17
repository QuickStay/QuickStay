package com.project.quickstay.service;

import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationServiceSelector {

    private final List<ReservationService> reservationServices;

    public ReservationService getService(Room room) {
        BookType bookType = getBookType(room);

        return reservationServices.stream()
                .filter(service -> service.supports(bookType))
                .findFirst()
                .orElseThrow(() -> new ServiceException("예약 서비스를 찾는데 실패하였습니다."));
    }

    private BookType getBookType(Room room) {
        System.out.println(room.getBooking().getBookType());
        return room.getBooking().getBookType();
    }
}
