package com.project.quickstay.domain.review.entity;

import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.review.dto.ReviewWrite;
import com.project.quickstay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    private String roomName;

    private int score;

    private String content;

    private LocalDateTime createdAt;

    public static Review writeReview(ReviewWrite reviewWrite, User user, Place place) {
        Review review = new Review();
        review.user = user;
        review.place = place;
        review.roomName = reviewWrite.getRoomName();
        review.score = reviewWrite.getScore();
        review.content = reviewWrite.getContent();
        review.createdAt = LocalDateTime.now();
        return review;
    }
}
