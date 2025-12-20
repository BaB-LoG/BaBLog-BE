package com.ssafy.bablog.meal_log.repository;

import com.ssafy.bablog.meal_log.domain.MealLog;
import com.ssafy.bablog.meal_log.repository.mapper.MealLogDailyTotal;

import java.time.LocalDate;
import java.util.List;

public interface MealLogRepository {
    void upsertNutrition(MealLog mealLog);

    List<MealLog> findByMealIds(List<Long> mealIds);

    MealLogDailyTotal findDailyTotal(Long memberId, LocalDate targetDate);

    List<MealLogDailyTotal> findDailyTotalsInRange(Long memberId, LocalDate from, LocalDate to);
}
