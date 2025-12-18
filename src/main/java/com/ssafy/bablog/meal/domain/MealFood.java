package com.ssafy.bablog.meal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealFood {
    private Long id;
    private Long mealId;
    private Long foodId;
    private BigDecimal intake; // 섭취량(g 등 단위)
    private String unit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(Long newFoodId, BigDecimal newIntake, String newUnit) {
        this.foodId = newFoodId;
        this.intake = newIntake;
        this.unit = newUnit;
    }
}
