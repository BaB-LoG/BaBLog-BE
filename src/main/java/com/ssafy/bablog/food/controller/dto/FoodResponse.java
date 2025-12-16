package com.ssafy.bablog.food.controller.dto;

import com.ssafy.bablog.food.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private BigDecimal standard;
    private String name;
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal saturatedFat;
    private BigDecimal transFat;
    private BigDecimal carbohydrates;
    private BigDecimal sugar;
    private BigDecimal natrium;
    private BigDecimal cholesterol;
    private BigDecimal foodWeight;
    private String vendor;

    public static FoodResponse from(Food food) {
        return new FoodResponse(
                food.getId(),
                food.getStandard(),
                food.getName(),
                food.getKcal(),
                food.getProtein(),
                food.getFat(),
                food.getSaturatedFat(),
                food.getTransFat(),
                food.getCarbohydrates(),
                food.getSugar(),
                food.getNatrium(),
                food.getCholesterol(),
                food.getFoodWeight(),
                food.getVendor()
        );
    }
}
