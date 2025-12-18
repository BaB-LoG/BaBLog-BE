package com.ssafy.bablog.member_nutrient.repository.mapper;

import com.ssafy.bablog.member_nutrient.domain.MemberNutrient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberNutrientMapper {
    MemberNutrient findByMemberId(@Param("memberId") Long memberId);

    void insert(MemberNutrient memberNutrient);

    void update(MemberNutrient memberNutrient);
}
