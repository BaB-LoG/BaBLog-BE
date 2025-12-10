package com.ssafy.bablog.meal.repository;

import com.ssafy.bablog.meal.domain.MealFood;
import com.ssafy.bablog.meal.repository.mapper.MealFoodWithFood;

import java.util.List;
import java.util.Optional;

public interface MealFoodRepository {
    MealFood save(MealFood mealFood);

    List<MealFoodWithFood> findByMealIdsWithFood(List<Long> mealIds);

    Optional<MealFood> findById(Long mealFoodId);

    void deleteById(Long mealFoodId);

    void update(MealFood mealFood);
}
