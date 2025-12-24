package com.ssafy.bablog.goal_history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarSummaryResponse {
    private LocalDate date;
    private int dailyTotal;
    private int dailyCompleted;
    private boolean hasDailyProgress;
    private String weeklyStatus; // "NONE", "IN_PROGRESS", "COMPLETED"
}
