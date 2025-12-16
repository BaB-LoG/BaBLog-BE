package com.ssafy.bablog.meal_log.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ssafy.bablog.food.domain.Food;
import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal.util.NutritionCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealLog {
    private Long id;
    private Long mealId;
    private Long memberId;
    private LocalDateTime loggedAt;
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal saturatedFat;
    private BigDecimal transFat;
    private BigDecimal carbohydrates;
    private BigDecimal sugar;
    private BigDecimal natrium;
    private BigDecimal cholesterol;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 식단에 음식 추가 시 이를 meal_log에 반영하기 위해 만드는 객체
    // 실제 meal_log에 데이터로 들어가는 meal_log가 아니라
    // meal_log에 더해지기 위한 meal_log를 만드는 메서드.
    public static MealLog from(Meal meal, Food food, BigDecimal intake) {
        return MealLog.builder()
                .mealId(meal.getId())
                .memberId(meal.getMemberId())
                .loggedAt(meal.getMealDate().atStartOfDay())
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
     * 영양 델타의 부호를 반전한 새 MealLog 생성 (감소용)
     */
    public MealLog reverseDelta() {
        return MealLog.builder()
                .mealId(this.mealId)
                .memberId(this.memberId)
                .loggedAt(this.loggedAt)
                .kcal(neg(this.kcal))
                .protein(neg(this.protein))
                .fat(neg(this.fat))
                .saturatedFat(neg(this.saturatedFat))
                .transFat(neg(this.transFat))
                .carbohydrates(neg(this.carbohydrates))
                .sugar(neg(this.sugar))
                .natrium(neg(this.natrium))
                .cholesterol(neg(this.cholesterol))
                .build();
    }

    private static BigDecimal neg(BigDecimal value) {
        return value == null ? null : value.negate();
    }
}
