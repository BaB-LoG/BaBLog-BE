package com.ssafy.bablog.meal.repository;

import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal.domain.MealType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepository {
    Meal save(Meal meal);

    Optional<Meal> findById(Long mealId);

    List<Meal> findByMemberAndDate(Long memberId, LocalDate mealDate);

    Optional<Meal> findByMemberAndTypeAndDate(Long memberId, MealType mealType, LocalDate mealDate);

    void adjustNutrition(Long mealId, Meal delta);
}
