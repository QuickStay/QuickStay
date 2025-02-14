package com.project.quickstay.domain.reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DayArticleForm {
    private LocalDate startDate;
    private LocalDate endDate;

    public DayArticleForm(LocalDate startDate,  LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "ArticleForm{" +
                "startDate=" + startDate +
                ", content=" + endDate +
                '}';
    }
}
