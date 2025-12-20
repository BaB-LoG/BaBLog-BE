package com.ssafy.bablog.report.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class WeeklyScoreResult {
    private int totalScore;
    private int averageScore;
    private int consistencyScore;
    private String grade;
    private List<String> riskFlags;
    private Map<String, Object> trend;
}
