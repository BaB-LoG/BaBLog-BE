package com.ssafy.bablog.member_nutrient.domain;

import com.ssafy.bablog.member_nutrient.controller.dto.UpdateMemberNutrientRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberNutrient {
    private Long id;
    private Long memberId;
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

    public void apply(UpdateMemberNutrientRequest request) {
        if (request == null) {
            return;
        }
        this.kcal = firstNonNull(request.getKcal(), this.kcal);
        this.protein = firstNonNull(request.getProtein(), this.protein);
        this.fat = firstNonNull(request.getFat(), this.fat);
        this.saturatedFat = firstNonNull(request.getSaturatedFat(), this.saturatedFat);
        this.transFat = firstNonNull(request.getTransFat(), this.transFat);
        this.carbohydrates = firstNonNull(request.getCarbohydrates(), this.carbohydrates);
        this.sugar = firstNonNull(request.getSugar(), this.sugar);
        this.natrium = firstNonNull(request.getNatrium(), this.natrium);
        this.cholesterol = firstNonNull(request.getCholesterol(), this.cholesterol);
    }

    private <T> T firstNonNull(T first, T second) {
        return Objects.nonNull(first) ? first : second;
    }
}
