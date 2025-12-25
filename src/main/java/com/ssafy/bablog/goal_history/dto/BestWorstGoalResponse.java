package com.ssafy.bablog.goal_history.dto;

import com.ssafy.bablog.goal_history.service.dto.BestWorstGoal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestWorstGoalResponse {

    private GoalHighlight bestGoal;
    private GoalHighlight hardestGoal;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoalHighlight {
        private Long goalId;
        private String title;
        private int maxStreak;
        private double avgAchievementRate;
    }

    public static BestWorstGoalResponse from(BestWorstGoal summary) {
        return BestWorstGoalResponse.builder()
                .bestGoal(fromHighlight(summary.getBestGoal()))
                .hardestGoal(fromHighlight(summary.getHardestGoal()))
                .build();
    }

    private static GoalHighlight fromHighlight(BestWorstGoal.GoalHighlight highlight) {
        if (highlight == null) {
            return null;
        }
        return GoalHighlight.builder()
                .goalId(highlight.getGoalId())
                .title(highlight.getTitle())
                .maxStreak(highlight.getMaxStreak())
                .avgAchievementRate(highlight.getAvgAchievementRate())
                .build();
    }
}
