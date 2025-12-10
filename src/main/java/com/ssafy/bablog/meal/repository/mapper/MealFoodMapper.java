package com.ssafy.bablog.meal.repository.mapper;

import com.ssafy.bablog.meal.domain.MealFood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MealFoodMapper {
    void insert(MealFood mealFood);

    List<MealFoodWithFood> findByMealIdsWithFood(@Param("mealIds") List<Long> mealIds);

    MealFood findById(@Param("id") Long id);

    void deleteById(@Param("id") Long id);

    void update(MealFood mealFood);
}
