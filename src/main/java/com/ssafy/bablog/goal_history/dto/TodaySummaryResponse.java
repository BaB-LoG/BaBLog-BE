package com.ssafy.bablog.goal_history.dto;

import com.ssafy.bablog.goal_history.service.dto.TodaySummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodaySummaryResponse {

    // 일일 목표 통계
    private int dailyCompleted;
    private int dailyInProgress;
    private double dailyAchievementRate;

    // 주간 목표 통계
    private int weeklyTotal;
    private int weeklyCompleted;
    private double weeklyAchievementRate;

    public static TodaySummaryResponse from(TodaySummary summary) {
        return TodaySummaryResponse.builder()
                .dailyCompleted(summary.getDailyCompleted())
                .dailyInProgress(summary.getDailyInProgress())
                .dailyAchievementRate(summary.getDailyAchievementRate())
                .weeklyTotal(summary.getWeeklyTotal())
                .weeklyCompleted(summary.getWeeklyCompleted())
                .weeklyAchievementRate(summary.getWeeklyAchievementRate())
                .build();
    }
}
