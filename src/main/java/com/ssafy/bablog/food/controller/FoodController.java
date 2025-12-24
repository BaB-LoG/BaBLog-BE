package com.ssafy.bablog.food.controller;

import com.ssafy.bablog.food.dto.FoodResponse;
import com.ssafy.bablog.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    /**
     * 음식명/제조사 키워드 검색 (부분 일치)
     */
    @GetMapping("/search")
    public ResponseEntity<List<FoodResponse>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "vendor", required = false) String vendor
    ) {
        List<FoodResponse> responses = foodService.search(name, vendor).stream()
                .map(FoodResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
