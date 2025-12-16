package com.ssafy.bablog.member_nutrient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberNutrientDaily {
    private Long id;
    private Long memberId;
    private LocalDate targetDate;
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
}
