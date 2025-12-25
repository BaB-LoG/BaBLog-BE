package com.ssafy.bablog.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DailyReportResponse {
    private LocalDate reportDate;
    private Integer score;
    private String grade;
    private String summary;
    private List<String> highlights;
    private List<String> improvements;
    private List<String> recommendations;
    private Map<String, Integer> nutrientScores;
    private List<String> riskFlags;
    private Map<String, Object> metrics;
    private LocalDateTime updatedAt;
}
