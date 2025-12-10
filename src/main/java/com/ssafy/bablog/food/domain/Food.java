package com.ssafy.bablog.food.domain;

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
public class Food {
    private Long id;
    private BigDecimal standard;
    private String name;
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbohydrates;
    private BigDecimal sugar;
    private BigDecimal natrium;
    private BigDecimal cholesterol;
    private BigDecimal saturatedFat;
    private BigDecimal transFat;
    private BigDecimal foodWeight;
    private String vendor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
