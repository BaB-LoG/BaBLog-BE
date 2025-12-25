package com.ssafy.bablog.meal.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private LocalDate date;
    private NutritionResponse totals;
    private NutritionResponse targets;
    private List<MealSummaryResponse> meals;
}
