package com.project.quickstay.controller;

import com.project.quickstay.domain.place.dto.PlaceSearch;
import com.project.quickstay.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlaceApiController {

    private final PlaceService placeService;

    @GetMapping("/api/place/search")
    public ResponseEntity<Page<PlaceSearch>> placeSearch(@RequestParam(defaultValue = "1", required = false) int page, @RequestParam String keyword) {
        Page<PlaceSearch> search = placeService.search(keyword, page);
        return ResponseEntity.ok(search);
    }
}
