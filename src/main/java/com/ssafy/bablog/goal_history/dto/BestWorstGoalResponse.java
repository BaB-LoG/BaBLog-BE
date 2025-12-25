package com.ssafy.bablog.goal_history.dto;

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
}
