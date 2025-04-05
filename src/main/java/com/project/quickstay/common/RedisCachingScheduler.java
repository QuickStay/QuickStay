package com.project.quickstay.common;

import com.project.quickstay.domain.place.dto.PlaceMiniInfo;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<PlaceMiniInfo> mostReserved = setRank(placeRepository.findFiveMostReservedPlace(), redisService.getValue(Constant.MOST_RESERVED));
        redisService.saveMainPageData(Constant.MOST_RESERVED, mostReserved);
    }

    private void saveHighestReview() {
        List<PlaceMiniInfo> highest = setRank(placeRepository.findFiveHighestReviewAveragePlace(), redisService.getValue(Constant.HIGHEST_REVIEW_AVERAGE));
        redisService.saveMainPageData(Constant.HIGHEST_REVIEW_AVERAGE, highest);
    }

    private void saveRandom() {
        List<PlaceMiniInfo> random = placeRepository.findFiveRandomPlace().stream()
                .map(PlaceMiniInfo::new)
                .toList();
        redisService.saveMainPageData(Constant.RANDOM, random);
    }

    private void saveTodayMostReserved() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<PlaceMiniInfo> newVal = placeRepository.findTenTodayMostReservedPlace(startOfDay, endOfDay)
                .stream()
                .map(PlaceMiniInfo::new)
                .toList();
        List<PlaceMiniInfo> oldVal = redisService.getValue(Constant.TODAY_MOST_RESERVED);
        List<PlaceMiniInfo> todayMostReserved = setRank(newVal, oldVal);
        redisService.saveMainPageData(Constant.TODAY_MOST_RESERVED, todayMostReserved);
    }

    private List<PlaceMiniInfo> setRank(List<PlaceMiniInfo> newVal, List<PlaceMiniInfo> oldVal) {
        Map<PlaceMiniInfo, Integer> oldRankMap = new HashMap<>();
        for (int i = 0; i < oldVal.size(); i++) {
            oldRankMap.put(oldVal.get(i), i);
        }

        for (int i = 0; i < newVal.size(); i++) {
            PlaceMiniInfo newInfo = newVal.get(i);
            Integer oldRank = oldRankMap.get(newInfo);

            if (oldRank != null) {
                int rank = oldRank - i;
                if (rank == 0) {
                    newInfo.setRankChanges("-");
                } else if (rank > 0) {
                    newInfo.setRankChanges("+" + rank);
                } else {
                    newInfo.setRankChanges(String.valueOf(rank));
                }
            } else {
                newInfo.setRankChanges("NEW");
            }
        }

        return newVal;
    }


}
