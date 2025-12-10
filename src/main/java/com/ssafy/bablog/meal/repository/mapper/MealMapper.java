package com.ssafy.bablog.meal.repository.mapper;

import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal.domain.MealType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MealMapper {
    void insert(Meal meal);

    Meal findById(@Param("id") Long id);

    List<Meal> findByMemberAndDate(@Param("memberId") Long memberId, @Param("mealDate") LocalDate mealDate);

    Meal findByMemberAndTypeAndDate(@Param("memberId") Long memberId,
                                    @Param("mealType") MealType mealType,
                                    @Param("mealDate") LocalDate mealDate);

    void adjustNutrition(@Param("id") Long id, @Param("delta") Meal delta);
}
