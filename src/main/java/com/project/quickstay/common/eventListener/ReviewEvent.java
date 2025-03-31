package com.project.quickstay.common.eventListener;

import com.project.quickstay.domain.place.entity.Place;
import lombok.Getter;

@Getter
public class ReviewEvent {

    private final Place place;
    private final int reviewScore;

    public ReviewEvent(Place place, int reviewScore) {
        this.place = place;
        this.reviewScore = reviewScore;
    }
}
