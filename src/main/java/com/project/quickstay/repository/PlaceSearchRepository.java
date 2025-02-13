package com.project.quickstay.repository;

import com.project.quickstay.domain.place.dto.PlaceSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceSearchRepository {

    Page<PlaceSearch> search(String keyword, Pageable pageable);
}
