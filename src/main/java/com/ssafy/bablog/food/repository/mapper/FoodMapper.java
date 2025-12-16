package com.ssafy.bablog.food.repository.mapper;

import com.ssafy.bablog.food.domain.Food;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodMapper {
    Food findById(@Param("id") Long id);

    List<Food> search(@Param("nameKeyword") String nameKeyword, @Param("vendorKeyword") String vendorKeyword);
}
