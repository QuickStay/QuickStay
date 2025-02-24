package com.project.quickstay.repository;

import com.project.quickstay.domain.room.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
