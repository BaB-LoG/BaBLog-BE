package com.ssafy.bablog.meal.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMealFoodRequest {

    @NotNull
    private Long mealId;

    private Long foodId;

    @DecimalMin(value = "0.0", inclusive = false, message = "섭취량은 0보다 커야 합니다.")
    private BigDecimal intake;

    private String unit;
}
