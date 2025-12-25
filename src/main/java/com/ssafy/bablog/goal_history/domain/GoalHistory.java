package com.ssafy.bablog.goal_history.domain;

import com.ssafy.bablog.goal.domain.GoalType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalHistory {

    private Long id;

    private Long goalId;
    private Long memberId;

    private GoalType goalType;
    private String title;

    private BigDecimal targetValue;
    private BigDecimal progressValue;

    private BigDecimal clickPerProgress;
    private Boolean isCompleted;

    private LocalDate recordDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
