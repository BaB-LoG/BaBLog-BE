package com.ssafy.bablog.meal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ssafy.bablog.food.domain.Food;
import com.ssafy.bablog.meal.util.NutritionCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    private Long id;
    private Long memberId;
    private MealType mealType;
    private LocalDate mealDate;
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbohydrates;
    private BigDecimal sugar;
    private BigDecimal natrium;
    private BigDecimal cholesterol;
    private BigDecimal saturatedFat;
    private BigDecimal transFat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 식단에 음식 추가 시 이를 meal에 반영하기 위해 만드는 객체
    // 실제 meal에 데이터로 들어가는 meal이 아니라
    // meal에 더해지기 위한 meal을 만드는 메서드.
    public static Meal nutritionDelta(Food food, BigDecimal intake) {
        return Meal.builder()
                .kcal(NutritionCalculator.scaleNutrient(food.getKcal(), food, intake))
                .protein(NutritionCalculator.scaleNutrient(food.getProtein(), food, intake))
                .fat(NutritionCalculator.scaleNutrient(food.getFat(), food, intake))
                .saturatedFat(NutritionCalculator.scaleNutrient(food.getSaturatedFat(), food, intake))
                .transFat(NutritionCalculator.scaleNutrient(food.getTransFat(), food, intake))
                .carbohydrates(NutritionCalculator.scaleNutrient(food.getCarbohydrates(), food, intake))
                .sugar(NutritionCalculator.scaleNutrient(food.getSugar(), food, intake))
                .natrium(NutritionCalculator.scaleNutrient(food.getNatrium(), food, intake))
                .cholesterol(NutritionCalculator.scaleNutrient(food.getCholesterol(), food, intake))
                .build();
    }

    /**
     * 현재 Meal에 델타를 반영 (영양 누적)
     */
    public void applyNutritionDelta(Meal delta) {
        this.kcal = clamp(sum(this.kcal, delta.getKcal()));
        this.protein = clamp(sum(this.protein, delta.getProtein()));
        this.fat = clamp(sum(this.fat, delta.getFat()));
        this.saturatedFat = clamp(sum(this.saturatedFat, delta.getSaturatedFat()));
        this.transFat = clamp(sum(this.transFat, delta.getTransFat()));
        this.carbohydrates = clamp(sum(this.carbohydrates, delta.getCarbohydrates()));
        this.sugar = clamp(sum(this.sugar, delta.getSugar()));
        this.natrium = clamp(sum(this.natrium, delta.getNatrium()));
        this.cholesterol = clamp(sum(this.cholesterol, delta.getCholesterol()));
    }

    /**
     * 전달된 델타의 부호를 반전한 새 델타 생성 (감소용)
     * 사용될 때는 nutritionDelta로 양의값을 만든 후 여기에 다시 넣어서 부호 반전
     */
    public static Meal reverseDelta(Meal delta) {
        return Meal.builder()
                .kcal(neg(delta.getKcal()))
                .protein(neg(delta.getProtein()))
                .fat(neg(delta.getFat()))
                .saturatedFat(neg(delta.getSaturatedFat()))
                .transFat(neg(delta.getTransFat()))
                .carbohydrates(neg(delta.getCarbohydrates()))
                .sugar(neg(delta.getSugar()))
                .natrium(neg(delta.getNatrium()))
                .cholesterol(neg(delta.getCholesterol()))
                .build();
    }

    private static BigDecimal sum(BigDecimal base, BigDecimal delta) {
        if (delta == null) return base;
        if (base == null) return delta;
        return base.add(delta);
    }

    private static BigDecimal neg(BigDecimal value) {
        return value == null ? null : value.negate();
    }

    private static BigDecimal clamp(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.signum() < 0 ? BigDecimal.ZERO : value;
    }
}
