package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BookingServiceSelector {

    private final Map<BookType, BookingService> maps;

    public BookingServiceSelector(List<BookingService> services) {
        this.maps = services.stream().collect(Collectors.toMap(BookingService::getType, service -> service));
    }

    public BookingService getService(BookType bookType) {
        BookingService service = maps.get(bookType);
        if (service == null) {
            throw new ServiceException("처리할 수 있는 BookingService가 없습니다.");
        }
        return service;
    }
}
