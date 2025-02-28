package com.project.quickstay.repository;

import com.project.quickstay.domain.place.dto.PlaceSearch;
import com.project.quickstay.domain.place.dto.QPlaceSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.quickstay.domain.place.entity.QPlace.place;

@Repository
@RequiredArgsConstructor
public class PlaceSearchRepositoryImpl implements PlaceSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<PlaceSearch> search(Long placeId, String keyword, int pageSize) {
         return jpaQueryFactory.select(new QPlaceSearch(place.id, place.user.nickname, place.name, place.description, place.address))
                .from(place)
                .where(ltPlaceId(placeId))
                .where(keywordEq(keyword))
                .orderBy(place.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression keywordEq(String keyword) {
        if (!keyword.isEmpty()) {
            return place.address.contains(keyword).or(place.name.contains(keyword)).or(place.description.contains(keyword));
        }
        return null;
    }

    private BooleanExpression ltPlaceId(Long placeId) {
        if (placeId == null) {
            return null;
        }

        return place.id.lt(placeId);
    }
}
