package com.ssafy.bablog.goal.dto;

import com.ssafy.bablog.goal.domain.GoalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class GoalCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotNull
    private GoalType goalType;

    @NotNull
    @Positive
    private BigDecimal targetValue;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    @Positive
    private BigDecimal clickPerProgress;

}
