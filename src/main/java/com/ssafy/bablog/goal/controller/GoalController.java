package com.ssafy.bablog.goal.controller;

import com.ssafy.bablog.goal.domain.GoalType;
import com.ssafy.bablog.goal.dto.GoalCreateRequest;
import com.ssafy.bablog.goal.dto.GoalResponse;
import com.ssafy.bablog.goal.dto.GoalUpdateRequest;
import com.ssafy.bablog.goal.service.dto.GoalCreateCommand;
import com.ssafy.bablog.goal.service.dto.GoalResult;
import com.ssafy.bablog.goal.service.dto.GoalUpdateCommand;
import com.ssafy.bablog.goal.service.GoalService;
import com.ssafy.bablog.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor // final 필드만을 매개변수로 받는 생성자 자동 생성

public class GoalController {
    private final GoalService goalService;

    // 목표 등록
    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(
            @AuthenticationPrincipal MemberPrincipal principal,
            @RequestBody @Valid GoalCreateRequest request) {
        Long memberId = principal.getId();
        GoalResult result = goalService.createGoal(memberId, new GoalCreateCommand(
                request.getTitle(),
                request.getGoalType(),
                request.getTargetValue(),
                request.getStartDate(),
                request.getEndDate(),
                request.getClickPerProgress()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(GoalResponse.from(result));
    }

    // 목표 목록 조회 (일일 / 주간)
    @GetMapping
    public ResponseEntity<List<GoalResponse>> getGoals(
            @AuthenticationPrincipal MemberPrincipal principal,
            @RequestParam GoalType goalType) {
        Long memberId = principal.getId();
        List<GoalResponse> goals = goalService.getGoals(memberId, goalType)
                .stream()
                .map(GoalResponse::from)
                .toList();
        return ResponseEntity.ok(goals);
    }

    // 목표 상세 조회
    @GetMapping("/{goalId}")
    public ResponseEntity<GoalResponse> getGoal(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable Long goalId) {
        Long memberId = principal.getId();
        GoalResult result = goalService.getGoal(memberId, goalId);
        return ResponseEntity.ok(GoalResponse.from(result));
    }

    // 목표 수정
    @PatchMapping("/{goalId}")
    public ResponseEntity<GoalResponse> updateGoal(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable Long goalId,
            @RequestBody @Valid GoalUpdateRequest request) {
        Long memberId = principal.getId();
        GoalResult result = goalService.updateGoal(memberId, goalId, new GoalUpdateCommand(
                request.getTitle(),
                request.getTargetValue(),
                request.getStartDate(),
                request.getEndDate(),
                request.getClickPerProgress()
        ));
        return ResponseEntity.ok(GoalResponse.from(result));
    }

    // 목표 삭제
    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable Long goalId) {
        Long memberId = principal.getId();
        goalService.deleteGoal(memberId, goalId);
        return ResponseEntity.noContent().build();
    }

    // 목표 진행도 증가
    @PatchMapping("/{goalId}/progress")
    public ResponseEntity<GoalResponse> increaseProgress(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable Long goalId) {
        Long memberId = principal.getId();
        GoalResult result = goalService.increaseProgress(memberId, goalId);
        return ResponseEntity.ok(GoalResponse.from(result));
    }

    @PatchMapping("/{goalId}/progress/decrease")
    public ResponseEntity<GoalResponse> decreaseProgress(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable Long goalId) {
        Long memberId = principal.getId();
        GoalResult result = goalService.decreaseProgress(memberId, goalId);
        return ResponseEntity.ok(GoalResponse.from(result));
    }


}
