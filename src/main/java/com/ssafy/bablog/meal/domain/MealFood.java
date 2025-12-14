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
    private BigDecimal quantity;
    private String unit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(Long newFoodId, BigDecimal newQuantity, String newUnit) {
        this.foodId = newFoodId;
        this.quantity = newQuantity;
        this.unit = newUnit;
    }
}
