package com.project.quickstay.controller;

import com.project.quickstay.domain.place.dto.PlaceSearch;
import com.project.quickstay.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PlaceApiController {

    private final PlaceService placeService;

    @GetMapping("/api/place/search")
    public ResponseEntity<List<PlaceSearch>> placeSearch(@RequestParam(required = false) Long last, @RequestParam String keyword) {
        List<PlaceSearch> search = placeService.search(keyword, last);
        return ResponseEntity.ok(search);
    }
}
