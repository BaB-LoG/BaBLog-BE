package com.ssafy.bablog.member_nutrient.repository;

import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberNutrientDailyRepository {
    void upsert(MemberNutrientDaily daily);

    Optional<MemberNutrientDaily> findByMemberAndDate(Long memberId, LocalDate date);

    List<MemberNutrientDaily> findRange(Long memberId, LocalDate from, LocalDate to);
}
