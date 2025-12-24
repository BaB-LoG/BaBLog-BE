package com.ssafy.bablog.member_nutrient.dto;

import com.ssafy.bablog.member_nutrient.domain.MemberNutrient;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemberNutrientResponse {
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
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static MemberNutrientResponse from(MemberNutrient nutrient) {
        if (nutrient == null) {
            return null;
        }
        return MemberNutrientResponse.builder()
                .memberId(nutrient.getMemberId())
                .targetDate(null)
                .kcal(nutrient.getKcal())
                .protein(nutrient.getProtein())
                .fat(nutrient.getFat())
                .saturatedFat(nutrient.getSaturatedFat())
                .transFat(nutrient.getTransFat())
                .carbohydrates(nutrient.getCarbohydrates())
                .sugar(nutrient.getSugar())
                .natrium(nutrient.getNatrium())
                .cholesterol(nutrient.getCholesterol())
                .createdAt(nutrient.getCreatedAt())
                .updatedAt(nutrient.getUpdatedAt())
                .build();
    }

}
