package com.ssafy.bablog.meal.controller.dto;

import com.ssafy.bablog.meal.domain.MealType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddMealFoodRequest {

    @NotNull
    private MealType mealType;

    @NotNull
    private LocalDate mealDate;

    @NotNull
    private Long foodId;

    @NotNull
    @DecimalMin(value = "0.01", message = "섭취량은 0보다 커야 합니다.")
    private BigDecimal intake; // 그램 등 절대량

    private String unit;
}
