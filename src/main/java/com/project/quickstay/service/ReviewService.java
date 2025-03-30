package com.project.quickstay.service;

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

    public ReviewWrite getWriteForm(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setPlaceId(reservation.getRoom().getPlace().getId());
        reviewWrite.setRoomName(reservation.getRoom().getName());
        return reviewWrite;
    }

    public Review writeReview(ReviewWrite reviewWrite, User user) {
        Review review = Review.writeReview(reviewWrite, user, getPlaceById(reviewWrite.getPlaceId()));
        return reviewRepository.save(review);
    }

    public List<ReviewInfo> getPlaceReview(Long placeId) {
        return reviewRepository.findReviewsByPlaceId(placeId);
    }

    private Reservation getReservationById(Long reservationId) {
        Optional<Reservation> getReservation = reservationRepository.findById(reservationId);
        if (getReservation.isEmpty()) {
            throw new ServiceException("예약이 없습니다.");
        }
        return getReservation.get();
    }

    private Place getPlaceById(Long placeId) {
        Optional<Place> getPlace = placeRepository.findById(placeId);
        if (getPlace.isEmpty()) {
            throw new ServiceException("장소가 없습니다.");
        }
        return getPlace.get();
    }
}
