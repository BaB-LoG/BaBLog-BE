package com.ssafy.bablog.goal.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoalUpdateRequest {

    @Size(max = 100)
    private String title;

    @Positive
    private BigDecimal targetValue;

    private LocalDate startDate;

    private LocalDate endDate;

    @Positive
    private BigDecimal clickPerProgress;
}
