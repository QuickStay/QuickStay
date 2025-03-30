package com.project.quickstay.domain.review.dto;

import com.project.quickstay.domain.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewInfo {
    private String username;
    private String roomName;
    private int score;
    private String content;
    private LocalDateTime createdAt;

    public ReviewInfo(Review review) {
        this.username = review.getUser().getNickname();
        this.roomName = review.getRoomName();
        this.score = review.getScore();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }
}
