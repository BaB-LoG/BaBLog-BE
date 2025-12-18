package com.ssafy.bablog.member_nutrient.repository;

import com.ssafy.bablog.member_nutrient.domain.MemberNutrient;

import java.util.Optional;

public interface MemberNutrientRepository {
    MemberNutrient save(MemberNutrient memberNutrient);

    void update(MemberNutrient memberNutrient);

    Optional<MemberNutrient> findByMemberId(Long memberId);
}
