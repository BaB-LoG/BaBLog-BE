package com.ssafy.bablog.member_nutrient.util;

import com.ssafy.bablog.member.domain.Gender;
import com.ssafy.bablog.member_nutrient.domain.MemberNutrient;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 성별/신장/체중을 기반으로 권장 섭취량을 계산한다.
 * - Mifflin-St Jeor식 기반, 가정: 나이 30세, 활동계수 1.2(좌식).
 * - 단백질: 체중(kg) * 0.8 g
 * - 지방: 총열량 25%, 포화지방 10%, 트랜스지방 1%
 * - 탄수화물: 총열량 55%
 * - 당류: 총열량 10%
 * - 나트륨: 2,000 mg, 콜레스테롤: 300 mg
 */
public final class MemberNutrientCalculator {

    private static final int ASSUMED_AGE = 30;
    private static final BigDecimal ACTIVITY_FACTOR = new BigDecimal("1.2");
    private static final BigDecimal PROTEIN_PER_KG = new BigDecimal("0.8");
    private static final BigDecimal FAT_RATIO = new BigDecimal("0.25");
    private static final BigDecimal SATURATED_FAT_RATIO = new BigDecimal("0.10");
    private static final BigDecimal TRANS_FAT_RATIO = new BigDecimal("0.01");
    private static final BigDecimal CARB_RATIO = new BigDecimal("0.55");
    private static final BigDecimal SUGAR_RATIO = new BigDecimal("0.10");
    private static final BigDecimal NINE = new BigDecimal("9");
    private static final BigDecimal FOUR = new BigDecimal("4");
    private static final BigDecimal DEFAULT_NATRIUM_MG = new BigDecimal("2000");
    private static final BigDecimal DEFAULT_CHOLESTEROL_MG = new BigDecimal("300");

    private MemberNutrientCalculator() {
    }

    public static MemberNutrient calculate(Gender gender, BigDecimal heightCm, BigDecimal weightKg, Long memberId) {
        BigDecimal kcal = calculateKcal(gender, heightCm, weightKg);
        return MemberNutrient.builder()
                .memberId(memberId)
                .kcal(kcal)
                .protein(scale(weightKg.multiply(PROTEIN_PER_KG)))
                .fat(scale(kcal.multiply(FAT_RATIO).divide(NINE, 10, RoundingMode.HALF_UP)))
                .saturatedFat(scale(kcal.multiply(SATURATED_FAT_RATIO).divide(NINE, 10, RoundingMode.HALF_UP)))
                .transFat(scale(kcal.multiply(TRANS_FAT_RATIO).divide(NINE, 10, RoundingMode.HALF_UP)))
                .carbohydrates(scale(kcal.multiply(CARB_RATIO).divide(FOUR, 10, RoundingMode.HALF_UP)))
                .sugar(scale(kcal.multiply(SUGAR_RATIO).divide(FOUR, 10, RoundingMode.HALF_UP)))
                .natrium(DEFAULT_NATRIUM_MG)
                .cholesterol(DEFAULT_CHOLESTEROL_MG)
                .build();
    }

    private static BigDecimal calculateKcal(Gender gender, BigDecimal heightCm, BigDecimal weightKg) {
        double base = 10 * weightKg.doubleValue()
                + 6.25 * heightCm.doubleValue()
                - 5 * ASSUMED_AGE
                + (gender == Gender.MALE ? 5 : -161);
        BigDecimal total = BigDecimal.valueOf(base).multiply(ACTIVITY_FACTOR);
        return scale(total);
    }

    private static BigDecimal scale(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
