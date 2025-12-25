package com.ssafy.bablog.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class WeeklyReportResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer score;
    private String grade;
    private String summary;
    private String patternSummary;
    private LocalDate bestDay;
    private String bestReason;
    private LocalDate worstDay;
    private String worstReason;
    private String nextWeekFocus;
    private List<String> highlights;
    private List<String> improvements;
    private List<String> recommendations;
    private Map<String, Object> trend;
    private List<String> riskFlags;
    private Integer consistencyScore;
    private List<WeeklyDailyScoreResponse> dailyScores;
    private LocalDateTime updatedAt;
}
