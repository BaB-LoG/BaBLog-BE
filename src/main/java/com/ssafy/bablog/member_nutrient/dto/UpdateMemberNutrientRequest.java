package com.ssafy.bablog.member_nutrient.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateMemberNutrientRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "kcal은 0 이상이어야 합니다.")
    private BigDecimal kcal;
    @DecimalMin(value = "0.0", inclusive = true, message = "단백질은 0 이상이어야 합니다.")
    private BigDecimal protein;
    @DecimalMin(value = "0.0", inclusive = true, message = "지방은 0 이상이어야 합니다.")
    private BigDecimal fat;
    @DecimalMin(value = "0.0", inclusive = true, message = "포화지방은 0 이상이어야 합니다.")
    private BigDecimal saturatedFat;
    @DecimalMin(value = "0.0", inclusive = true, message = "트랜스지방은 0 이상이어야 합니다.")
    private BigDecimal transFat;
    @DecimalMin(value = "0.0", inclusive = true, message = "탄수화물은 0 이상이어야 합니다.")
    private BigDecimal carbohydrates;
    @DecimalMin(value = "0.0", inclusive = true, message = "당류는 0 이상이어야 합니다.")
    private BigDecimal sugar;
    @DecimalMin(value = "0.0", inclusive = true, message = "나트륨은 0 이상이어야 합니다.")
    private BigDecimal natrium;
    @DecimalMin(value = "0.0", inclusive = true, message = "콜레스테롤은 0 이상이어야 합니다.")
    private BigDecimal cholesterol;

    public boolean hasAnyValue() {
        return kcal != null || protein != null || fat != null || saturatedFat != null
                || transFat != null || carbohydrates != null || sugar != null
                || natrium != null || cholesterol != null;
    }
}
