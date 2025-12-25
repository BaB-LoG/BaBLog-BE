package com.ssafy.bablog.goal_history.dto;

import com.ssafy.bablog.goal.domain.GoalType;
import com.ssafy.bablog.goal_history.service.dto.GoalStats;
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

    public static GoalStatsResponse from(GoalStats stats) {
        return GoalStatsResponse.builder()
                .goalId(stats.getGoalId())
                .title(stats.getTitle())
                .maxStreak(stats.getMaxStreak())
                .avgAchievementRate(stats.getAvgAchievementRate())
                .goalType(stats.getGoalType())
                .build();
    }
}
