package com.ssafy.bablog.meal.controller.dto;

import com.ssafy.bablog.food.domain.Food;
import com.ssafy.bablog.meal.domain.MealFood;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import com.ssafy.bablog.meal.util.NutritionCalculator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealFoodResponse {
    private Long mealId;
    private Long foodId;
    private Long mealFoodId;
    private BigDecimal standard;
    private String name;
    private BigDecimal intake;
    private String unit;
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbohydrates;
    private BigDecimal sugar;
    private BigDecimal natrium;
    private BigDecimal cholesterol;
    private BigDecimal saturatedFat;
    private BigDecimal transFat;
    private String vendor;

    public static MealFoodResponse from(MealFood mealFood, Food food) {
        return new MealFoodResponse(
                mealFood.getMealId(),
                mealFood.getFoodId(),
                mealFood.getId(),
                food.getStandard(),
                food.getName(),
                mealFood.getIntake(),
                mealFood.getUnit(),
                NutritionCalculator.scaleNutrient(food.getKcal(), food, mealFood.getIntake()),
                NutritionCalculator.scaleNutrient(food.getProtein(), food, mealFood.getIntake()),
                NutritionCalculator.scaleNutrient(food.getFat(), food, mealFood.getIntake()),
                NutritionCalculator.scaleNutrient(food.getCarbohydrates(), food, mealFood.getIntake()),
                NutritionCalculator.scaleNutrient(food.getSugar(), food, mealFood.getIntake()),
                NutritionCalculator.scaleNutrient(food.getNatrium(), food, mealFood.getIntake()),
                NutritionCalculator.scaleNutrient(food.getCholesterol(), food, mealFood.getIntake()),
                NutritionCalculator.scaleNutrient(food.getSaturatedFat(), food, mealFood.getIntake()),
                NutritionCalculator.scaleNutrient(food.getTransFat(), food, mealFood.getIntake()),
                food.getVendor()
        );
    }
}
