package com.ssafy.bablog.meal_log.repository;

import com.ssafy.bablog.meal_log.domain.MealLog;

public interface MealLogRepository {
    void upsertNutrition(MealLog mealLog);
}
