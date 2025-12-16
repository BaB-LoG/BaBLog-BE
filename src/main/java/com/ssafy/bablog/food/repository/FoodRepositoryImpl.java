package com.ssafy.bablog.food.repository;

import com.ssafy.bablog.food.domain.Food;
import com.ssafy.bablog.food.repository.mapper.FoodMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FoodRepositoryImpl implements FoodRepository {

    private final FoodMapper foodMapper;

    public FoodRepositoryImpl(FoodMapper foodMapper) {
        this.foodMapper = foodMapper;
    }

    @Override
    public Optional<Food> findById(Long foodId) {
        return Optional.ofNullable(foodMapper.findById(foodId));
    }

    @Override
    public List<Food> search(String nameKeyword, String vendorKeyword) {
        return foodMapper.search(nameKeyword, vendorKeyword);
    }
}
