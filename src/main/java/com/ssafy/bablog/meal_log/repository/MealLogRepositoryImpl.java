package com.ssafy.bablog.meal_log.repository;

import com.ssafy.bablog.meal_log.domain.MealLog;
import com.ssafy.bablog.meal_log.repository.mapper.MealLogMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MealLogRepositoryImpl implements MealLogRepository {

    private final MealLogMapper mealLogMapper;

    public MealLogRepositoryImpl(MealLogMapper mealLogMapper) {
        this.mealLogMapper = mealLogMapper;
    }

    @Override
    @Transactional
    public void upsertNutrition(MealLog mealLog) {
        mealLogMapper.upsert(mealLog);
    }

    @Override
    public java.util.List<MealLog> findByMealIds(java.util.List<Long> mealIds) {
        if (mealIds == null || mealIds.isEmpty()) {
            return java.util.List.of();
        }
        return mealLogMapper.findByMealIds(mealIds);
    }

}
