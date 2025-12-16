package com.ssafy.bablog.meal_log.repository;

import com.ssafy.bablog.meal_log.domain.MealLog;

import java.util.List;

public interface MealLogRepository {
    void upsertNutrition(MealLog mealLog);

    List<MealLog> findByMealIds(List<Long> mealIds);
}
