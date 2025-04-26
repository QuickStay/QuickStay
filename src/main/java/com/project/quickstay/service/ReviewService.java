package com.project.quickstay.service;

import com.project.quickstay.common.eventListener.EventHandler;
import com.project.quickstay.common.eventListener.ReviewEvent;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.domain.review.dto.ReviewInfo;
import com.project.quickstay.domain.review.dto.ReviewWrite;
import com.project.quickstay.domain.review.entity.Review;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.repository.ReservationRepository;
import com.project.quickstay.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final EventHandler eventHandler;

    public ReviewWrite getWriteForm(Long reservationId, User user) {
        validUser(reservationId, user);
        Reservation reservation = getReservationById(reservationId);
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setPlaceId(reservation.getRoom().getPlace().getId());
        reviewWrite.setRoomName(reservation.getRoom().getName());
        return reviewWrite;
    }

    public Review writeReview(ReviewWrite reviewWrite, User user) {
        Review review = Review.writeReview(reviewWrite, user, getPlaceById(reviewWrite.getPlaceId()));
        eventHandler.handleReviewWrite(new ReviewEvent(review.getPlace(), reviewWrite.getScore()));
        return reviewRepository.save(review);
    }

    public List<ReviewInfo> getPlaceReview(Long placeId) {
        return reviewRepository.findReviewsByPlaceId(placeId);
    }

    private Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new ServiceException("예약이 없습니다."));
    }

    private Place getPlaceById(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(() -> new ServiceException("장소가 없습니다."));
    }

    private void validUser(Long reservationId, User user) {
        Reservation reservation = getReservationById(reservationId);
        if (!user.equals(reservation.getUser())) {
            throw new ServiceException("권한이 없습니다.");
        }
    }
}
