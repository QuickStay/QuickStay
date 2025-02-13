package com.project.quickstay.repository;

import com.project.quickstay.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select count(*) from Room r where r.place.id=:placeId")
    int getRoomCountByPlaceId(Long placeId);

    @Query("select r from Room r where r.place.id=:placeId")
    List<Room> findRoomsByPlaceId(Long placeId);
}
