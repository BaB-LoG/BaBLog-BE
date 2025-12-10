package com.ssafy.bablog.meal.util;

import com.ssafy.bablog.food.domain.Food;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 음식 영양 값을 섭취량에 맞게 스케일링하는 유틸리티.
 * - food.standard: 영양 기준량(예: 100g). 양의 정수(BigDecimal)로 보장됨.
 * - food.foodWeight: 실제 제공량(예: 500g) - 단위 동일 가정.
 * - quantity: 1이면 foodWeight만큼 섭취했다는 의미.
 *   예) standard=100, foodWeight=500, quantity=1 => 스케일 팩터 = 5.0
 */
public final class NutritionCalculator {

    private static final MathContext MC = MathContext.DECIMAL128;

    private NutritionCalculator() {
    }

    public static BigDecimal scaleNutrient(BigDecimal nutrient, Food food, BigDecimal quantity) {
        BigDecimal factor = computeFactor(food, quantity);
        if (nutrient == null || factor == null) {
            return null;
        }
        return nutrient.multiply(factor, MC);
    }

    private static BigDecimal computeFactor(Food food, BigDecimal quantity) {
        BigDecimal qty = quantity == null ? BigDecimal.ONE : quantity;
        BigDecimal foodWeight = food.getFoodWeight();
        BigDecimal standardWeight = food.getStandard();

        if (foodWeight != null && standardWeight != null && standardWeight.signum() > 0) {
            return qty.multiply(foodWeight, MC).divide(standardWeight, MC);
        }
        return qty;
    }

}
