package com.project.quickstay.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewWrite {

    private Long placeId;
    private String roomName;
    private int score;
    private String content;
}
