package com.ssafy.bablog.member_nutrient.controller;

import com.ssafy.bablog.member_nutrient.controller.dto.MemberNutrientDailyResponse;
import com.ssafy.bablog.member_nutrient.controller.dto.MemberNutrientResponse;
import com.ssafy.bablog.member_nutrient.controller.dto.UpdateMemberNutrientRequest;
import com.ssafy.bablog.member_nutrient.domain.MemberNutrient;
import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;
import com.ssafy.bablog.member_nutrient.service.MemberNutrientService;
import com.ssafy.bablog.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/members/nutrients")
@RequiredArgsConstructor
public class MemberNutrientController {

    private final MemberNutrientService memberNutrientService;

    /**
     * 권장 섭취량 조회
     */
    @GetMapping
    public ResponseEntity<MemberNutrientResponse> getMemberNutrient(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberNutrient nutrient = memberNutrientService.getMemberNutrient(principal.getId());
        return ResponseEntity.ok(MemberNutrientResponse.from(nutrient));
    }

    /**
     * 키/체중 기반 권장 섭취량 재계산 & 저장
     */
    @PostMapping("/recalculate")
    public ResponseEntity<MemberNutrientResponse> recalculate(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberNutrient nutrient = memberNutrientService.recalculate(principal.getId());
        return ResponseEntity.ok(MemberNutrientResponse.from(nutrient));
    }

    /**
     * 사용자 직접 수정
     */
    @PatchMapping
    public ResponseEntity<MemberNutrientResponse> manualUpdate(@AuthenticationPrincipal MemberPrincipal principal,
                                                               @Valid @RequestBody UpdateMemberNutrientRequest request) {
        MemberNutrient nutrient = memberNutrientService.manualUpdate(principal.getId(), request);
        return ResponseEntity.ok(MemberNutrientResponse.from(nutrient));
    }

    /**
     * 특정 날짜의 일별 목표 영양 섭취량 조회
     */
    @GetMapping("/daily")
    public ResponseEntity<MemberNutrientDailyResponse> getDaily(@AuthenticationPrincipal MemberPrincipal principal,
                                                                @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        MemberNutrientDaily daily = memberNutrientService.getDaily(principal.getId(), date);
        return ResponseEntity.ok(MemberNutrientDailyResponse.from(daily));
    }
}
