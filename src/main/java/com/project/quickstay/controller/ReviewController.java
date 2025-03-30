package com.project.quickstay.controller;

import com.project.quickstay.common.Login;
import com.project.quickstay.domain.review.dto.ReviewWrite;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/review/{reservationId}/write")
    public String writeReviewForm(@PathVariable Long reservationId, Model model) {
        ReviewWrite writeForm = reviewService.getWriteForm(reservationId);
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("write", writeForm);
        return "review/reviewWrite";
    }

    @PostMapping("/review/{reservationId}/write")
    public String writeReview(@PathVariable Long reservationId, @Login User user, @ModelAttribute("write") ReviewWrite reviewWrite) {
        reviewService.writeReview(reviewWrite, user);
        return "redirect:/home";
    }
}
