package com.ssafy.bablog.food.repository.mapper;

import com.ssafy.bablog.food.domain.Food;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FoodMapper {
    Food findById(@Param("id") Long id);
}
