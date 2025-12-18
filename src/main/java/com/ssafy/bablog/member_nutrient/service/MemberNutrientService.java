package com.ssafy.bablog.member_nutrient.service;

import com.ssafy.bablog.member.domain.Member;
import com.ssafy.bablog.member.repository.MemberRepository;
import com.ssafy.bablog.member_nutrient.controller.dto.UpdateMemberNutrientRequest;
import com.ssafy.bablog.member_nutrient.domain.MemberNutrient;
import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;
import com.ssafy.bablog.member_nutrient.repository.MemberNutrientRepository;
import com.ssafy.bablog.member_nutrient.repository.MemberNutrientDailyRepository;
import com.ssafy.bablog.member_nutrient.util.MemberNutrientCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberNutrientService {

    private final MemberNutrientRepository memberNutrientRepository;
    private final MemberNutrientDailyRepository memberNutrientDailyRepository;
    private final MemberRepository memberRepository;

    /**
     * 가입 시 키/몸무게가 모두 존재하면 권장 섭취량을 저장한다.
     */
    public void createIfPossible(Member member) {
        if (member == null || member.getHeightCm() == null || member.getWeightKg() == null) {
            return;
        }
        MemberNutrient calculated = MemberNutrientCalculator.calculate(member.getGender(), member.getHeightCm(), member.getWeightKg(), member.getId());
        MemberNutrient saved = upsert(calculated);
        upsertDaily(member.getId(), LocalDate.now(), saved);
    }

    /**
     * 마이페이지에서 키/몸무게 업데이트 후 호출: 권장 섭취량 재계산 및 저장.
     */
    public MemberNutrient recalculate(Long memberId) {
        Member member = getMember(memberId);
        if (member.getHeightCm() == null || member.getWeightKg() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권장 섭취량 계산을 위해 키와 체중이 모두 필요합니다.");
        }
        MemberNutrient calculated = MemberNutrientCalculator.calculate(member.getGender(), member.getHeightCm(), member.getWeightKg(), memberId);
        MemberNutrient saved = upsert(calculated);
        upsertDaily(memberId, LocalDate.now(), saved);
        return saved;
    }

    /**
     * 사용자 임의 수정.
     */
    public MemberNutrient manualUpdate(Long memberId, UpdateMemberNutrientRequest request) {
        if (request == null || !request.hasAnyValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 영양 성분 값을 최소 1개 이상 입력해야 합니다.");
        }
        Member member = getMember(memberId);

        MemberNutrient nutrient = memberNutrientRepository.findByMemberId(member.getId())
                .orElse(MemberNutrient.builder().memberId(member.getId()).build());
        nutrient.apply(request);

        if (nutrient.getId() == null) {
            memberNutrientRepository.save(nutrient);
        } else {
            memberNutrientRepository.update(nutrient);
        }
        upsertDaily(member.getId(), LocalDate.now(), nutrient);
        return nutrient;
    }

    public MemberNutrient getMemberNutrient(Long memberId) {
        return memberNutrientRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권장 섭취량 정보를 찾을 수 없습니다."));
    }

    /**
     * 특정 날짜의 목표 스냅샷 조회
     */
    @Transactional(readOnly = true)
    public MemberNutrientDaily getDaily(Long memberId, LocalDate date) {
        // 존재하는 회원인지 확인
        getMember(memberId);

        // 1순위: 일별 스냅샷
        Optional<MemberNutrientDaily> daily = memberNutrientDailyRepository.findByMemberAndDate(memberId, date);
        if (daily.isPresent()) {
            return daily.get();
        }

        // 2순위: 현재 member_nutrient를 날짜에 맞춰 스냅샷 형태로 반환
        Optional<MemberNutrient> base = memberNutrientRepository.findByMemberId(memberId);
        if (base.isPresent()) {
            MemberNutrient nutrient = base.get();
            return MemberNutrientDaily.builder()
                    .memberId(memberId)
                    .targetDate(date)
                    .kcal(nutrient.getKcal())
                    .protein(nutrient.getProtein())
                    .fat(nutrient.getFat())
                    .saturatedFat(nutrient.getSaturatedFat())
                    .transFat(nutrient.getTransFat())
                    .carbohydrates(nutrient.getCarbohydrates())
                    .sugar(nutrient.getSugar())
                    .natrium(nutrient.getNatrium())
                    .cholesterol(nutrient.getCholesterol())
                    .build();
        }

        // 3순위: 아무 데이터도 없으면 0으로 채운 스냅샷 반환
        return MemberNutrientDaily.builder()
                .memberId(memberId)
                .targetDate(date)
                .kcal(BigDecimal.ZERO)
                .protein(BigDecimal.ZERO)
                .fat(BigDecimal.ZERO)
                .saturatedFat(BigDecimal.ZERO)
                .transFat(BigDecimal.ZERO)
                .carbohydrates(BigDecimal.ZERO)
                .sugar(BigDecimal.ZERO)
                .natrium(BigDecimal.ZERO)
                .cholesterol(BigDecimal.ZERO)
                .build();
    }

    // -------------------- private --------------------

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
    }

    private MemberNutrient upsert(MemberNutrient calculated) {
        Optional<MemberNutrient> existing = memberNutrientRepository.findByMemberId(calculated.getMemberId());
        if (existing.isPresent()) {
            MemberNutrient persisted = existing.get();
            calculated.setId(persisted.getId());
            memberNutrientRepository.update(calculated);
            return calculated;
        }
        memberNutrientRepository.save(calculated);
        return calculated;
    }

    public void ensureTodaySnapshot(Long memberId) {
        Member member = getMember(memberId);
        memberNutrientRepository.findByMemberId(memberId).ifPresent(n -> upsertDaily(memberId, LocalDate.now(), n));
    }

    private void upsertDaily(Long memberId, LocalDate date, MemberNutrient nutrient) {
        if (nutrient == null) {
            return;
        }
        MemberNutrientDaily daily = MemberNutrientDaily.builder()
                .memberId(memberId)
                .targetDate(date)
                .kcal(nutrient.getKcal())
                .protein(nutrient.getProtein())
                .fat(nutrient.getFat())
                .saturatedFat(nutrient.getSaturatedFat())
                .transFat(nutrient.getTransFat())
                .carbohydrates(nutrient.getCarbohydrates())
                .sugar(nutrient.getSugar())
                .natrium(nutrient.getNatrium())
                .cholesterol(nutrient.getCholesterol())
                .build();
        memberNutrientDailyRepository.upsert(daily);
    }
}
