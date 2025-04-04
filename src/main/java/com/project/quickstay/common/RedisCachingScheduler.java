package com.project.quickstay.common;

import com.project.quickstay.domain.place.dto.PlaceMiniInfo;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCachingScheduler {

    private final PlaceRepository placeRepository;
    private final RedisService redisService;

    @Scheduled(cron = "0 0 * * * *") //매 시간마다 실행.
    @Transactional
    public void caching() {
        log.info("숙소 추천 데이터 캐싱 스케줄러 작동");
        saveMostReserved();
        saveHighestReview();
        saveRandom();
        saveTodayMostReserved();
    }

    private void saveMostReserved() {
        List<PlaceMiniInfo> mostReserved = placeRepository.findFiveMostReservedPlace();
        redisService.saveMainPageData(Constant.MOST_RESERVED, mostReserved);
    }

    private void saveHighestReview() {
        List<PlaceMiniInfo> highest = placeRepository.findFiveHighestReviewAveragePlace();
        redisService.saveMainPageData(Constant.HIGHEST_REVIEW_AVERAGE, highest);
    }

    private void saveRandom() {
        List<Place> temp = placeRepository.findFiveRandomPlace();
        List<PlaceMiniInfo> toList = temp.stream().map(PlaceMiniInfo::new).toList();
        redisService.saveMainPageData(Constant.RANDOM, toList);
    }

    private void saveTodayMostReserved() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<PlaceMiniInfo> toList = placeRepository.findTenTodayMostReservedPlace(startOfDay, endOfDay)
                .stream()
                .map(PlaceMiniInfo::new)
                .toList();
        redisService.saveMainPageData(Constant.TODAY_MOST_RESERVED, toList);
    }


}
