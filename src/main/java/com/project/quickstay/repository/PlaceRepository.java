package com.project.quickstay.repository;

import com.project.quickstay.domain.place.dto.MyPlaceInfo;
import com.project.quickstay.domain.place.dto.PlaceMiniInfo;
import com.project.quickstay.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceSearchRepository{

    @Query("select new com.project.quickstay.domain.place.dto.MyPlaceInfo(p) from Place p where p.user.id=:userId")
    List<MyPlaceInfo> getMyPlace(Long userId);

    int countByUserId(Long userId);

    @Query("select new com.project.quickstay.domain.place.dto.PlaceMiniInfo(p) from Place p order by p.reservedCount desc limit 5")
    List<PlaceMiniInfo> findFiveMostReservedPlace();

    @Query("select new com.project.quickstay.domain.place.dto.PlaceMiniInfo(p) from Place p order by p.reviewAverage desc limit 5")
    List<PlaceMiniInfo> findFiveHighestReviewAveragePlace();

    @Query(value = "select * from place order by RAND() limit 5", nativeQuery = true)
    List<Place> findFiveRandomPlace();

    @Query(value = "SELECT new com.project.quickstay.domain.place.dto.PlaceMiniInfo(p) " +
            "FROM ( " +
            "SELECT ro.place.id AS pid, COUNT(*) AS cnt " +
            "FROM Reservation r " +
            "JOIN Room ro ON r.room.id = ro.id " +
            "WHERE r.createdAt BETWEEN :startOfDay AND :endOfDay " +
            "AND r.state = 'RESERVED' " +
            "GROUP BY ro.place.id " +
            "ORDER BY cnt DESC " +
            "LIMIT 10 " +
            ") AS top_places " +
            "JOIN Place p ON p.id = top_places.pid")
    List<PlaceMiniInfo> findTenTodayMostReservedPlace(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}