package com.ssafy.bablog.meal.controller;

import com.ssafy.bablog.meal.dto.AddMealFoodRequest;
import com.ssafy.bablog.meal.dto.AddMealFoodResponse;
import com.ssafy.bablog.meal.dto.DashboardSummaryResponse;
import com.ssafy.bablog.meal.dto.MealWithFoodsResponse;
import com.ssafy.bablog.meal.dto.UpdateMealFoodRequest;
import com.ssafy.bablog.meal.service.MealService;
import com.ssafy.bablog.meal.service.dto.MealFoodAddCommand;
import com.ssafy.bablog.meal.service.dto.MealFoodUpdateCommand;
import com.ssafy.bablog.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final MealResponseMapper responseMapper;

    /**
     * Member 1명의 meal을 수동생성하는 메서드
     * 테스트를 위해 만든 api
     */
    @PostMapping("/test-api")
    public void createDailyMeals(@AuthenticationPrincipal MemberPrincipal principal){
        mealService.createDailyMeals(principal.getId(), LocalDate.now());
    }

    /**
     * 식단에 음식 추가 및 영양 누적
     */
    @PostMapping("/foods")
    public ResponseEntity<AddMealFoodResponse> addMealFood(@AuthenticationPrincipal MemberPrincipal principal,
                                                           @Valid @RequestBody AddMealFoodRequest request) {
        MealFoodAddCommand command = new MealFoodAddCommand(
                request.getMealType(),
                request.getMealDate(),
                request.getFoodId(),
                request.getIntake(),
                request.getUnit()
        );
        return ResponseEntity.ok(responseMapper.toAddMealFoodResponse(
                mealService.addFoodToMeal(principal.getId(), command)
        ));
    }

    /**
     * 날짜별 식단 목록 조회 (BREAK_FAST, LUNCH, DINNER, SNACK 모두 조회)
     */
    @GetMapping
    public ResponseEntity<List<MealWithFoodsResponse>> getMeals(@AuthenticationPrincipal MemberPrincipal principal,
                                                       @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mealDate) {
        return ResponseEntity.ok(responseMapper.toMealWithFoodsResponses(
                mealService.getMeals(principal.getId(), mealDate)
        ));
    }

    /**
     * 대시보드용 일자 요약
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getDailySummary(@AuthenticationPrincipal MemberPrincipal principal,
                                                                    @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mealDate) {
        return ResponseEntity.ok(responseMapper.toDashboardSummaryResponse(
                mealService.getDailySummary(principal.getId(), mealDate)
        ));
    }

    /** 식단 단건 조회 (BREAK_FAST 단일, LUNCH 단일 등) */
    @GetMapping("/{mealId}")
    public ResponseEntity<MealWithFoodsResponse> getMeal(@AuthenticationPrincipal MemberPrincipal principal,
                                                         @PathVariable Long mealId) {
        return ResponseEntity.ok(responseMapper.toMealWithFoodsResponse(
                mealService.getMeal(principal.getId(), mealId)
        ));
    }

    /**
     * 식단에 추가된 음식 삭제
     */
    @DeleteMapping("/foods/{mealFoodId}")
    public ResponseEntity<Void> deleteMealFood(@AuthenticationPrincipal MemberPrincipal principal,
                                               @PathVariable Long mealFoodId) {
        mealService.deleteMealFood(principal.getId(), mealFoodId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 식단에 추가된 음식 수정
     */
    @PatchMapping("/foods/{mealFoodId}")
    public ResponseEntity<MealWithFoodsResponse> updateMealFood(@AuthenticationPrincipal MemberPrincipal principal,
                                                                @PathVariable Long mealFoodId,
                                                                @Valid @RequestBody UpdateMealFoodRequest request) {
        MealFoodUpdateCommand command = new MealFoodUpdateCommand(
                request.getMealId(),
                request.getFoodId(),
                request.getIntake(),
                request.getUnit()
        );
        return ResponseEntity.ok(responseMapper.toMealWithFoodsResponse(
                mealService.updateMealFood(principal.getId(), mealFoodId, command)
        ));
    }
}
