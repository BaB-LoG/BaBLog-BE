package com.ssafy.bablog.goal_history.dto;

import com.ssafy.bablog.goal.domain.GoalType;
import com.ssafy.bablog.goal_history.domain.GoalHistory;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class GoalHistoryResponse {

    private Long id;
    private GoalType goalType;
    private String title;

    private BigDecimal targetValue;
    private BigDecimal progressValue;
    private BigDecimal clickPerProgress;

    private Boolean isCompleted;
    private LocalDate recordDate;

    public static GoalHistoryResponse from(GoalHistory history) {
        return GoalHistoryResponse.builder()
                .id(history.getId())
                .goalType(history.getGoalType())
                .title(history.getTitle())
                .targetValue(history.getTargetValue())
                .progressValue(history.getProgressValue())
                .clickPerProgress(history.getClickPerProgress())
                .isCompleted(history.getIsCompleted())
                .recordDate(history.getRecordDate())
                .build();
    }
}
