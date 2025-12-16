package com.ssafy.bablog.member_nutrient.repository;

import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;
import com.ssafy.bablog.member_nutrient.repository.mapper.MemberNutrientDailyMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberNutrientDailyRepositoryImpl implements MemberNutrientDailyRepository {

    private final MemberNutrientDailyMapper mapper;

    public MemberNutrientDailyRepositoryImpl(MemberNutrientDailyMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void upsert(MemberNutrientDaily daily) {
        mapper.upsert(daily);
    }

    @Override
    public Optional<MemberNutrientDaily> findByMemberAndDate(Long memberId, LocalDate date) {
        return Optional.ofNullable(mapper.findByMemberAndDate(memberId, date));
    }

    @Override
    public List<MemberNutrientDaily> findRange(Long memberId, LocalDate from, LocalDate to) {
        return mapper.findRange(memberId, from, to);
    }
}
