package com.ssafy.bablog.report.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DailyScoreResult {
    private int totalScore;
    private String grade;
    private Map<String, Integer> nutrientScores;
    private List<String> riskFlags;
    private Map<String, Object> metrics;
}
