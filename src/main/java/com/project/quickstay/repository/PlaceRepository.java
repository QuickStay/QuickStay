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

    @Query(value = "select * from PLACE order by RAND() limit 5", nativeQuery = true)
    List<Place> findFiveRandomPlace();

    @Query(value = "SELECT p.*" +
            "FROM Reservation r " +
            "JOIN Room ro ON r.room_id = ro.id " +
            "JOIN Place p ON ro.place_id = p.id " +
            "WHERE r.created_at BETWEEN :startOfDay AND :endOfDay " +
            "AND r.state = 'RESERVED' " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(r.id) DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Place> findTenTodayMostReservedPlace(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}