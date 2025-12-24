package com.ssafy.bablog.meal.dto;

import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal.domain.MealType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealResponse {
    private Long id;
    private MealType mealType;
    private LocalDate mealDate;
    private NutritionResponse nutrition;
    private List<MealFoodResponse> foods;

    public static MealResponse from(Meal meal) {
        return new MealResponse(
                meal.getId(),
                meal.getMealType(),
                meal.getMealDate(),
                NutritionResponse.fromMeal(meal),
                null
        );
    }
}
