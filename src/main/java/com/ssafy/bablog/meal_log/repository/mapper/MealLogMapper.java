package com.ssafy.bablog.meal_log.repository.mapper;

import com.ssafy.bablog.meal_log.domain.MealLog;
import com.ssafy.bablog.meal_log.repository.mapper.MealLogDailyTotal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MealLogMapper {
    void upsert(MealLog mealLog);

    List<MealLog> findByMealIds(List<Long> mealIds);

    MealLogDailyTotal findDailyTotal(@Param("memberId") Long memberId,
                                     @Param("targetDate") LocalDate targetDate);

    List<MealLogDailyTotal> findDailyTotalsInRange(@Param("memberId") Long memberId,
                                                   @Param("from") LocalDate from,
                                                   @Param("to") LocalDate to);
}
