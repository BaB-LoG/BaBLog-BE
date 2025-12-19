package com.ssafy.bablog.meal.controller.dto;

import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal_log.domain.MealLog;
import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutritionResponse {
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal saturatedFat;
    private BigDecimal transFat;
    private BigDecimal carbohydrates;
    private BigDecimal sugar;
    private BigDecimal natrium;
    private BigDecimal cholesterol;

    public static NutritionResponse from(MealLog mealLog) {
        return Optional.ofNullable(mealLog)
                .map(log -> new NutritionResponse(
                        log.getKcal(),
                        log.getProtein(),
                        log.getFat(),
                        log.getSaturatedFat(),
                        log.getTransFat(),
                        log.getCarbohydrates(),
                        log.getSugar(),
                        log.getNatrium(),
                        log.getCholesterol()
                ))
                .orElse(new NutritionResponse(null, null, null, null, null, null, null, null, null));
    }

    public static NutritionResponse fromMeal(Meal meal) {
        return Optional.ofNullable(meal)
                .map(m -> new NutritionResponse(
                        m.getKcal(),
                        m.getProtein(),
                        m.getFat(),
                        m.getSaturatedFat(),
                        m.getTransFat(),
                        m.getCarbohydrates(),
                        m.getSugar(),
                        m.getNatrium(),
                        m.getCholesterol()
                ))
                .orElse(new NutritionResponse(null, null, null, null, null, null, null, null, null));
    }

    public static NutritionResponse fromMemberNutrientDaily(MemberNutrientDaily daily) {
        return Optional.ofNullable(daily)
                .map(d -> new NutritionResponse(
                        d.getKcal(),
                        d.getProtein(),
                        d.getFat(),
                        d.getSaturatedFat(),
                        d.getTransFat(),
                        d.getCarbohydrates(),
                        d.getSugar(),
                        d.getNatrium(),
                        d.getCholesterol()
                ))
                .orElse(new NutritionResponse(null, null, null, null, null, null, null, null, null));
    }
}
