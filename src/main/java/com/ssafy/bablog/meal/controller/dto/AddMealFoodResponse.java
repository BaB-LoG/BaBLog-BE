package com.ssafy.bablog.meal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddMealFoodResponse {
    private MealResponse meal;
    private MealFoodResponse mealFood;

    public static AddMealFoodResponse of(MealResponse mealResponse, MealFoodResponse mealFoodResponse) {
        return new AddMealFoodResponse(mealResponse, mealFoodResponse);
    }
}
