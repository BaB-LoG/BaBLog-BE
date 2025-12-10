package com.ssafy.bablog.meal.repository.mapper;

import com.ssafy.bablog.food.domain.Food;
import com.ssafy.bablog.meal.domain.MealFood;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealFoodWithFood {
    private MealFood mealFood;
    private Food food;
}
