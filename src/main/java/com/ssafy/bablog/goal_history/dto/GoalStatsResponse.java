package com.ssafy.bablog.goal_history.dto;

import com.ssafy.bablog.goal.domain.GoalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalStatsResponse {
    private Long goalId;
    private String title;
    private int maxStreak; // 이번 달 최대 연속 달성일수
    private double avgAchievementRate; // 이번 달 평균 달성률
    private GoalType goalType;
}
