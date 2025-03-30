package com.project.quickstay.repository;

import com.project.quickstay.domain.review.dto.ReviewInfo;
import com.project.quickstay.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select new com.project.quickstay.domain.review.dto.ReviewInfo(r) from Review r where r.place.id=:placeId")
    List<ReviewInfo> findReviewsByPlaceId(Long placeId);
}
