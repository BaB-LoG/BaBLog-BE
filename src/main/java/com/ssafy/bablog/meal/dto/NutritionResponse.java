package com.ssafy.bablog.meal.dto;

import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal_log.domain.MealLog;
import com.ssafy.bablog.meal.service.dto.NutritionSummary;
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

    public static NutritionResponse zero() {
        return new NutritionResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    public void addFromMeal(Meal meal) {
        if (meal == null) {
            return;
        }
        this.kcal = orZero(this.kcal).add(orZero(meal.getKcal()));
        this.protein = orZero(this.protein).add(orZero(meal.getProtein()));
        this.fat = orZero(this.fat).add(orZero(meal.getFat()));
        this.saturatedFat = orZero(this.saturatedFat).add(orZero(meal.getSaturatedFat()));
        this.transFat = orZero(this.transFat).add(orZero(meal.getTransFat()));
        this.carbohydrates = orZero(this.carbohydrates).add(orZero(meal.getCarbohydrates()));
        this.sugar = orZero(this.sugar).add(orZero(meal.getSugar()));
        this.natrium = orZero(this.natrium).add(orZero(meal.getNatrium()));
        this.cholesterol = orZero(this.cholesterol).add(orZero(meal.getCholesterol()));
    }

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

    public static NutritionResponse fromSummary(NutritionSummary summary) {
        return Optional.ofNullable(summary)
                .map(s -> new NutritionResponse(
                        s.getKcal(),
                        s.getProtein(),
                        s.getFat(),
                        s.getSaturatedFat(),
                        s.getTransFat(),
                        s.getCarbohydrates(),
                        s.getSugar(),
                        s.getNatrium(),
                        s.getCholesterol()
                ))
                .orElse(new NutritionResponse(null, null, null, null, null, null, null, null, null));
    }

    private static BigDecimal orZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
