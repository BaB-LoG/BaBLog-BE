package com.ssafy.bablog.meal.util;

import com.ssafy.bablog.food.domain.Food;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 음식 영양 값을 섭취량(intake)에 맞게 스케일링하는 유틸리티.
 * - food.standard: 영양 표기 기준량(예: 100g).
 * - intake: 실제 섭취량(그램 등 절대량). intake/standard로 스케일 팩터 계산.
 */
public final class NutritionCalculator {

    private static final MathContext MC = MathContext.DECIMAL128;

    private NutritionCalculator() {
    }

    public static BigDecimal scaleNutrient(BigDecimal nutrient, Food food, BigDecimal intake) {
        BigDecimal factor = computeFactor(food, intake);
        if (nutrient == null || factor == null) {
            return null;
        }
        return nutrient.multiply(factor, MC);
    }

    private static BigDecimal computeFactor(Food food, BigDecimal intake) {
        BigDecimal intakeAmount = intake == null ? BigDecimal.ZERO : intake;
        BigDecimal standardWeight = food.getStandard();

        if (standardWeight != null && standardWeight.signum() > 0) {
            return intakeAmount.divide(standardWeight, MC);
        }
        return intakeAmount;
    }

}
