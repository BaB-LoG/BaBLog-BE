package com.ssafy.bablog.goal.dto;

import com.ssafy.bablog.goal.domain.GoalType;
import com.ssafy.bablog.goal.service.dto.GoalResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponse {
    private Long id;
    private GoalType goalType;
    private String title;
    private BigDecimal targetValue;
    private BigDecimal progressValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal clickPerProgress;
    private Boolean isCompleted;

    public static GoalResponse from(GoalResult result) {
        return GoalResponse.builder()
                .id(result.getId())
                .goalType(result.getGoalType())
                .title(result.getTitle())
                .targetValue(result.getTargetValue())
                .progressValue(result.getProgressValue())
                .startDate(result.getStartDate())
                .endDate(result.getEndDate())
                .clickPerProgress(result.getClickPerProgress())
                .isCompleted(result.getIsCompleted())
                .build();
    }
}
