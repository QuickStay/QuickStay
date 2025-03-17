package com.project.quickstay.service;

import com.project.quickstay.domain.room.entity.BookType;

import java.time.temporal.Temporal;
import java.util.List;

public interface ReservationService {

    boolean supports(BookType bookType);

    List<? extends Temporal> getReserved(Long roomId);

}
