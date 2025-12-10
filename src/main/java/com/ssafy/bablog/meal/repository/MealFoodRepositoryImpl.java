package com.ssafy.bablog.meal.repository;

import com.ssafy.bablog.meal.domain.MealFood;
import com.ssafy.bablog.meal.repository.mapper.MealFoodMapper;
import com.ssafy.bablog.meal.repository.mapper.MealFoodWithFood;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class MealFoodRepositoryImpl implements MealFoodRepository {

    private final MealFoodMapper mealFoodMapper;

    public MealFoodRepositoryImpl(MealFoodMapper mealFoodMapper) {
        this.mealFoodMapper = mealFoodMapper;
    }

    @Override
    @Transactional
    public MealFood save(MealFood mealFood) {
        mealFoodMapper.insert(mealFood);
        return mealFood;
    }

    @Override
    public List<MealFoodWithFood> findByMealIdsWithFood(java.util.List<Long> mealIds) {
        return mealFoodMapper.findByMealIdsWithFood(mealIds);
    }

    @Override
    public java.util.Optional<MealFood> findById(Long mealFoodId) {
        return java.util.Optional.ofNullable(mealFoodMapper.findById(mealFoodId));
    }

    @Override
    @Transactional
    public void deleteById(Long mealFoodId) {
        mealFoodMapper.deleteById(mealFoodId);
    }

    @Override
    @Transactional
    public void update(MealFood mealFood) {
        mealFoodMapper.update(mealFood);
    }
}
