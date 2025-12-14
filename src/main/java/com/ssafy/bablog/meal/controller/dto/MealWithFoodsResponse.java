package com.ssafy.bablog.meal.controller.dto;

import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal.domain.MealType;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealWithFoodsResponse {
    private Long id;
    private MealType mealType;
    private LocalDate mealDate;
    private NutritionResponse nutrition; // getMeal에서는 사용하지 않음
    private List<MealFoodResponse> foods;

    public static MealWithFoodsResponse withNutrition(Meal meal, List<MealFoodResponse> foods) {
        return new MealWithFoodsResponse(
                meal.getId(),
                meal.getMealType(),
                meal.getMealDate(),
                NutritionResponse.fromMeal(meal),
                foods
        );
    }

    public static MealWithFoodsResponse withoutNutrition(Meal meal, List<MealFoodResponse> foods) {
        return new MealWithFoodsResponse(
                meal.getId(),
                meal.getMealType(),
                meal.getMealDate(),
                null,
                foods
        );
    }
}
