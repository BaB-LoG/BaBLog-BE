package com.ssafy.bablog.member_nutrient.repository;

import com.ssafy.bablog.member_nutrient.domain.MemberNutrient;
import com.ssafy.bablog.member_nutrient.repository.mapper.MemberNutrientMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class MemberNutrientRepositoryImpl implements MemberNutrientRepository {

    private final MemberNutrientMapper memberNutrientMapper;

    public MemberNutrientRepositoryImpl(MemberNutrientMapper memberNutrientMapper) {
        this.memberNutrientMapper = memberNutrientMapper;
    }

    @Override
    @Transactional
    public MemberNutrient save(MemberNutrient memberNutrient) {
        memberNutrientMapper.insert(memberNutrient);
        return memberNutrient;
    }

    @Override
    @Transactional
    public void update(MemberNutrient memberNutrient) {
        memberNutrientMapper.update(memberNutrient);
    }

    @Override
    public Optional<MemberNutrient> findByMemberId(Long memberId) {
        return Optional.ofNullable(memberNutrientMapper.findByMemberId(memberId));
    }
}
