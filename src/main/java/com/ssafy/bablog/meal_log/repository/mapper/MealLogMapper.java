package com.ssafy.bablog.meal_log.repository.mapper;

import com.ssafy.bablog.meal_log.domain.MealLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MealLogMapper {
    void upsert(MealLog mealLog);

    List<MealLog> findByMealIds(List<Long> mealIds);
}
