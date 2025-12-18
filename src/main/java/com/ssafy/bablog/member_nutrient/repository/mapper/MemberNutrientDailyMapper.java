package com.ssafy.bablog.member_nutrient.repository.mapper;

import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MemberNutrientDailyMapper {
    void upsert(MemberNutrientDaily daily);

    MemberNutrientDaily findByMemberAndDate(@Param("memberId") Long memberId, @Param("targetDate") LocalDate targetDate);

    List<MemberNutrientDaily> findRange(@Param("memberId") Long memberId,
                                        @Param("from") LocalDate from,
                                        @Param("to") LocalDate to);
}
