package com.ssafy.bablog.food.repository;

import com.ssafy.bablog.food.domain.Food;

import java.util.List;
import java.util.Optional;

public interface FoodRepository {
    Optional<Food> findById(Long foodId);

    List<Food> search(String nameKeyword, String vendorKeyword);
}
