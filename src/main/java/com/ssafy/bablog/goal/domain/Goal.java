package com.ssafy.bablog.goal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Goal {

    private Long id;
    private Long memberId;
    private GoalType goalType;
    private String title;
    private BigDecimal targetValue;
    private BigDecimal progressValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal clickPerProgress;
    private boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
