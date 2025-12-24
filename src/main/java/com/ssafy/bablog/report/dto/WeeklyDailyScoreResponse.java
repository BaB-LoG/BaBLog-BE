package com.ssafy.bablog.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class WeeklyDailyScoreResponse {
    private LocalDate date;
    private Integer score;
}
