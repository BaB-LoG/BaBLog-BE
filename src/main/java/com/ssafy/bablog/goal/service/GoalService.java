package com.ssafy.bablog.goal.service;


import com.ssafy.bablog.goal.domain.GoalType;
import com.ssafy.bablog.goal.dto.GoalCreateRequest;
import com.ssafy.bablog.goal.dto.GoalResponse;
import com.ssafy.bablog.goal.dto.GoalUpdateRequest;

import java.util.List;

public interface GoalService {
    GoalResponse createGoal(Long memberId, GoalCreateRequest request);

    List<GoalResponse> getGoals(Long memberId, GoalType goalType);

    GoalResponse getGoal(Long memberId, Long goalId);

    GoalResponse updateGoal(Long memberId, Long goalId, GoalUpdateRequest request);

    void deleteGoal(Long memberId, Long goalId);

    GoalResponse increaseProgress(Long memberId, Long goalId);
}

