package com.project.quickstay.repository;

import com.project.quickstay.domain.place.dto.MyPlaceInfo;
import com.project.quickstay.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceSearchRepository{

    @Query("select new com.project.quickstay.domain.place.dto.MyPlaceInfo(p) from Place p where p.user.id=:userId")
    List<MyPlaceInfo> getMyPlace(Long userId);

    int countByUserId(Long userId);
}