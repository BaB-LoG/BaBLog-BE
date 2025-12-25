package com.ssafy.bablog.meal.dto;

import com.ssafy.bablog.meal.domain.MealType;
import com.ssafy.bablog.meal.service.dto.MealSummary;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealSummaryResponse {
    private MealType mealType;
    private LocalDate mealDate;
    private int foodCount;
    private String representativeFoodName;
    private BigDecimal kcal;

    public static MealSummaryResponse from(MealSummary summary) {
        return new MealSummaryResponse(
                summary.getMealType(),
                summary.getMealDate(),
                summary.getFoodCount(),
                summary.getRepresentative(),
                summary.getKcal()
        );
    }
}
