package com.ssafy.bablog.meal.repository;

import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal.domain.MealType;
import com.ssafy.bablog.meal.repository.mapper.MealMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class MealRepositoryImpl implements MealRepository {

    private final MealMapper mealMapper;

    public MealRepositoryImpl(MealMapper mealMapper) {
        this.mealMapper = mealMapper;
    }

    @Override
    @Transactional
    public Meal save(Meal meal) {
        mealMapper.insert(meal);
        return meal;
    }

    @Override
    public Optional<Meal> findById(Long mealId) {
        return Optional.ofNullable(mealMapper.findById(mealId));
    }

    @Override
    public List<Meal> findByMemberAndDate(Long memberId, LocalDate mealDate) {
        return mealMapper.findByMemberAndDate(memberId, mealDate);
    }

    @Override
    public Optional<Meal> findByMemberAndTypeAndDate(Long memberId, MealType mealType, LocalDate mealDate) {
        return Optional.ofNullable(mealMapper.findByMemberAndTypeAndDate(memberId, mealType, mealDate));
    }

    @Override
    @Transactional
    public void adjustNutrition(Long mealId, Meal delta) {
        mealMapper.adjustNutrition(mealId, delta);
    }
}
