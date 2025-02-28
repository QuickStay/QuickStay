package com.project.quickstay.repository;

import com.project.quickstay.domain.place.dto.PlaceSearch;

import java.util.List;

public interface PlaceSearchRepository {

    List<PlaceSearch> search(Long placeId, String keyword, int pageSize);
}
