package com.ssafy.bablog.goal.service;

import com.ssafy.bablog.goal.domain.Goal;
import com.ssafy.bablog.goal.domain.GoalType;
import com.ssafy.bablog.goal.dto.GoalCreateRequest;
import com.ssafy.bablog.goal.dto.GoalResponse;
import com.ssafy.bablog.goal.dto.GoalUpdateRequest;
import com.ssafy.bablog.goal.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    //목표 등록
    @Override
    public GoalResponse createGoal(Long memberId, GoalCreateRequest request) {

        // 시작일 < 종료일
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("시작일은 종료일보다 늦을 수 없습니다.");
        }

        Goal goal = Goal.builder()
                .memberId(memberId)
                .title(request.getTitle())
                .goalType(request.getGoalType())
                .targetValue(request.getTargetValue())
                .progressValue(BigDecimal.ZERO)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .clickPerProgress(request.getClickPerProgress())
                .isCompleted(false)
                .build();


        goalRepository.insertGoal(goal);

        return GoalResponse.builder()
                .id(goal.getId())
                .goalType(goal.getGoalType())
                .title(goal.getTitle())
                .targetValue(goal.getTargetValue())
                .progressValue(goal.getProgressValue())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .clickPerProgress(goal.getClickPerProgress())
                .isCompleted(goal.isCompleted())
                .build();

    }

    @Override
    public List<GoalResponse> getGoals(Long memberId, GoalType goalType) {
        List<Goal> goals = goalRepository.findByMemberIdAndGoalType(memberId, goalType);

        return goals.stream()
                .map(goal -> GoalResponse.builder()
                        .id(goal.getId())
                        .goalType(goal.getGoalType())
                        .title(goal.getTitle())
                        .targetValue(goal.getTargetValue())
                        .progressValue(goal.getProgressValue())
                        .startDate(goal.getStartDate())
                        .endDate(goal.getEndDate())
                        .clickPerProgress(goal.getClickPerProgress())
                        .isCompleted(goal.isCompleted())
                        .build()
                )
                .toList();
    }

    @Override
    public GoalResponse getGoal(Long memberId, Long goalId) {

        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("목표를 찾을 수 없습니다."));

        return GoalResponse.builder()
                .id(goal.getId())
                .goalType(goal.getGoalType())
                .title(goal.getTitle())
                .targetValue(goal.getTargetValue())
                .progressValue(goal.getProgressValue())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .clickPerProgress(goal.getClickPerProgress())
                .isCompleted(goal.isCompleted())
                .build();
    }


    @Override
    public GoalResponse updateGoal(Long memberId, Long goalId, GoalUpdateRequest request) {

        // 조회 먼저 실행 ( 존재 여부, 사용자 id 일치, 수정 권한 검증 )
        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("목표를 찾을 수 없습니다."));


        //시작일 < 종료일
        LocalDate startDate =
                request.getStartDate() != null
                        ? request.getStartDate()
                        : goal.getStartDate();

        LocalDate endDate =
                request.getEndDate() != null
                        ? request.getEndDate()
                        : goal.getEndDate();

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일은 종료일보다 늦을 수 없습니다.");
        }


        // null 아닐때만 반영
        if (request.getTitle() != null) {
            goal.setTitle(request.getTitle());
        }

        if (request.getTargetValue() != null) {
            goal.setTargetValue(request.getTargetValue());
        }

        if (request.getStartDate() != null) {
            goal.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            goal.setEndDate(request.getEndDate());
        }

        if (request.getClickPerProgress() != null) {
            goal.setClickPerProgress(request.getClickPerProgress());
        }

        // 수정 쿼리 실행
        goalRepository.updateGoal(goal);

        return GoalResponse.builder()
                .id(goal.getId())
                .goalType(goal.getGoalType())
                .title(goal.getTitle())
                .targetValue(goal.getTargetValue())
                .progressValue(goal.getProgressValue())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .clickPerProgress(goal.getClickPerProgress())
                .isCompleted(goal.isCompleted())
                .build();
    }

    @Override
    public void deleteGoal(Long memberId, Long goalId) {

        // 조회 먼저 실행 ( 존재 여부, 사용자 id 일치 )
        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("목표를 찾을 수 없습니다."));

        int deleted = goalRepository.deleteByIdAndMemberId(goalId, memberId);

        if (deleted == 0) {
            throw new IllegalStateException("목표 삭제에 실패했습니다.");
        }
    }

    @Override
    public GoalResponse increaseProgress(Long memberId, Long goalId) {

        // 조회 먼저 실행 ( 존재 여부, 사용자 id 일치 )
        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("목표를 찾을 수 없습니다."));

        // 이미 완료된 목표면 더 이상 증가시키지 않음
        if (goal.isCompleted()) {
            return GoalResponse.builder()
                    .id(goal.getId())
                    .goalType(goal.getGoalType())
                    .title(goal.getTitle())
                    .targetValue(goal.getTargetValue())
                    .progressValue(goal.getProgressValue())
                    .startDate(goal.getStartDate())
                    .endDate(goal.getEndDate())
                    .clickPerProgress(goal.getClickPerProgress())
                    .isCompleted(true)
                    .build();
        }

        // 진행도 증가
        BigDecimal newProgress =
                goal.getProgressValue().add(goal.getClickPerProgress());

        // 목표 초과 방지 (최대 targetValue까지만)
        if (newProgress.compareTo(goal.getTargetValue()) >= 0) {
            newProgress = goal.getTargetValue();
            goal.setCompleted(true);
        }

        goal.setProgressValue(newProgress);

        // 진행도 증가 실행
        goalRepository.updateProgress(goal);


        return GoalResponse.builder()
                .id(goal.getId())
                .goalType(goal.getGoalType())
                .title(goal.getTitle())
                .targetValue(goal.getTargetValue())
                .progressValue(goal.getProgressValue())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .clickPerProgress(goal.getClickPerProgress())
                .isCompleted(goal.isCompleted())
                .build();
    }

}
