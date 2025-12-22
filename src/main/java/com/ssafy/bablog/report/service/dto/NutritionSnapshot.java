package com.ssafy.bablog.report.service.dto;

import com.ssafy.bablog.meal_log.repository.mapper.MealLogDailyTotal;
import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Builder
public class NutritionSnapshot {
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal saturatedFat;
    private BigDecimal transFat;
    private BigDecimal carbohydrates;
    private BigDecimal sugar;
    private BigDecimal natrium;
    private BigDecimal cholesterol;

    public static NutritionSnapshot from(MealLogDailyTotal total) {
        if (total == null) {
            return empty();
        }
        return NutritionSnapshot.builder()
                .kcal(zeroIfNull(total.getKcal()))
                .protein(zeroIfNull(total.getProtein()))
                .fat(zeroIfNull(total.getFat()))
                .saturatedFat(zeroIfNull(total.getSaturatedFat()))
                .transFat(zeroIfNull(total.getTransFat()))
                .carbohydrates(zeroIfNull(total.getCarbohydrates()))
                .sugar(zeroIfNull(total.getSugar()))
                .natrium(zeroIfNull(total.getNatrium()))
                .cholesterol(zeroIfNull(total.getCholesterol()))
                .build();
    }

    public static NutritionSnapshot from(MemberNutrientDaily daily) {
        if (daily == null) {
            return empty();
        }
        return NutritionSnapshot.builder()
                .kcal(zeroIfNull(daily.getKcal()))
                .protein(zeroIfNull(daily.getProtein()))
                .fat(zeroIfNull(daily.getFat()))
                .saturatedFat(zeroIfNull(daily.getSaturatedFat()))
                .transFat(zeroIfNull(daily.getTransFat()))
                .carbohydrates(zeroIfNull(daily.getCarbohydrates()))
                .sugar(zeroIfNull(daily.getSugar()))
                .natrium(zeroIfNull(daily.getNatrium()))
                .cholesterol(zeroIfNull(daily.getCholesterol()))
                .build();
    }

    public static NutritionSnapshot empty() {
        return NutritionSnapshot.builder()
                .kcal(BigDecimal.ZERO)
                .protein(BigDecimal.ZERO)
                .fat(BigDecimal.ZERO)
                .saturatedFat(BigDecimal.ZERO)
                .transFat(BigDecimal.ZERO)
                .carbohydrates(BigDecimal.ZERO)
                .sugar(BigDecimal.ZERO)
                .natrium(BigDecimal.ZERO)
                .cholesterol(BigDecimal.ZERO)
                .build();
    }

    public boolean isEmpty() {
        return isZero(kcal) && isZero(protein) && isZero(fat) && isZero(carbohydrates) && isZero(sugar)
                && isZero(natrium) && isZero(cholesterol) && isZero(saturatedFat) && isZero(transFat);
    }

    private static boolean isZero(BigDecimal value) {
        return value == null || BigDecimal.ZERO.compareTo(value) == 0;
    }

    private static BigDecimal zeroIfNull(BigDecimal value) {
        return Objects.requireNonNullElse(value, BigDecimal.ZERO);
    }
}
