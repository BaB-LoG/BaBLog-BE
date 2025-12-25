package com.ssafy.bablog.meal.controller;

import com.ssafy.bablog.meal.dto.AddMealFoodResponse;
import com.ssafy.bablog.meal.dto.DashboardSummaryResponse;
import com.ssafy.bablog.meal.dto.MealFoodResponse;
import com.ssafy.bablog.meal.dto.MealSummaryResponse;
import com.ssafy.bablog.meal.dto.MealWithFoodsResponse;
import com.ssafy.bablog.meal.dto.NutritionResponse;
import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal.repository.mapper.MealFoodWithFood;
import com.ssafy.bablog.meal.service.dto.DashboardSummary;
import com.ssafy.bablog.meal.service.dto.MealAggregate;
import com.ssafy.bablog.meal.service.dto.MealFoodAddition;
import com.ssafy.bablog.meal.service.dto.MealSummary;
import com.ssafy.bablog.meal_log.domain.MealLog;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MealResponseMapper {

    public AddMealFoodResponse toAddMealFoodResponse(MealFoodAddition addition) {
        return AddMealFoodResponse.of(
                com.ssafy.bablog.meal.dto.MealResponse.from(addition.getMeal()),
                MealFoodResponse.from(addition.getMealFood(), addition.getFood())
        );
    }

    public MealWithFoodsResponse toMealWithFoodsResponse(MealAggregate aggregate) {
        Meal meal = aggregate.getMeal();
        MealLog log = aggregate.getMealLog();
        List<MealFoodResponse> foods = mapFoods(aggregate.getFoods());
        return MealWithFoodsResponse.withNutrition(meal, foods, log);
    }

    public List<MealWithFoodsResponse> toMealWithFoodsResponses(List<MealAggregate> aggregates) {
        return aggregates.stream()
                .map(this::toMealWithFoodsResponse)
                .toList();
    }

    public DashboardSummaryResponse toDashboardSummaryResponse(DashboardSummary summary) {
        return new DashboardSummaryResponse(
                summary.getMealDate(),
                NutritionResponse.fromSummary(summary.getTotals()),
                NutritionResponse.fromSummary(summary.getTargets()),
                summary.getSummaries().stream().map(this::toMealSummaryResponse).toList()
        );
    }

    private MealSummaryResponse toMealSummaryResponse(MealSummary summary) {
        return new MealSummaryResponse(
                summary.getMealType(),
                summary.getMealDate(),
                summary.getFoodCount(),
                summary.getRepresentative(),
                summary.getKcal()
        );
    }

    private List<MealFoodResponse> mapFoods(List<MealFoodWithFood> foods) {
        if (foods == null) {
            return List.of();
        }
        return foods.stream()
                .map(row -> MealFoodResponse.from(row.getMealFood(), row.getFood()))
                .toList();
    }
}
