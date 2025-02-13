package com.project.quickstay.repository;

import com.project.quickstay.domain.place.dto.PlaceSearch;
import com.project.quickstay.domain.place.dto.QPlaceSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.quickstay.domain.place.entity.QPlace.place;

@Repository
@RequiredArgsConstructor
public class PlaceSearchRepositoryImpl implements PlaceSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<PlaceSearch> search(String keyword, Pageable pageable) {
        List<PlaceSearch> data = jpaQueryFactory.select(new QPlaceSearch(place.id, place.user.nickname, place.name, place.description, place.address))
                .from(place)
                .where(keywordEq(keyword))
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(place.count())
                .from(place)
                .where(keywordEq(keyword))
                .fetchOne();

        total = (total != null) ? total : 0L;

        return new PageImpl<>(data, pageable, total);
    }

    private BooleanExpression keywordEq(String keyword) {
        if (!keyword.isEmpty()) {
            return place.address.contains(keyword).or(place.name.contains(keyword)).or(place.description.contains(keyword));
        }
        return null;
    }

}
