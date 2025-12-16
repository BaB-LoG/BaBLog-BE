package com.ssafy.bablog.member_nutrient.controller.dto;

import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class MemberNutrientDailyResponse {
    private final Long memberId;
    private final LocalDate targetDate;
    private final BigDecimal kcal;
    private final BigDecimal protein;
    private final BigDecimal fat;
    private final BigDecimal saturatedFat;
    private final BigDecimal transFat;
    private final BigDecimal carbohydrates;
    private final BigDecimal sugar;
    private final BigDecimal natrium;
    private final BigDecimal cholesterol;

    public static MemberNutrientDailyResponse from(MemberNutrientDaily daily) {
        return MemberNutrientDailyResponse.builder()
                .memberId(daily.getMemberId())
                .targetDate(daily.getTargetDate())
                .kcal(daily.getKcal())
                .protein(daily.getProtein())
                .fat(daily.getFat())
                .saturatedFat(daily.getSaturatedFat())
                .transFat(daily.getTransFat())
                .carbohydrates(daily.getCarbohydrates())
                .sugar(daily.getSugar())
                .natrium(daily.getNatrium())
                .cholesterol(daily.getCholesterol())
                .build();
    }
}
