package com.ssafy.bablog.report.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.bablog.report.domain.DailyReport;
import com.ssafy.bablog.report.domain.WeeklyReport;
import com.ssafy.bablog.report.dto.DailyReportResponse;
import com.ssafy.bablog.report.dto.WeeklyDailyScoreResponse;
import com.ssafy.bablog.report.dto.WeeklyReportResponse;
import com.ssafy.bablog.report.repository.DailyReportRepository;
import com.ssafy.bablog.report.service.dto.DailyScorePoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReportResponseMapper {

    private final DailyReportRepository dailyReportRepository;
    private final ObjectMapper objectMapper;

    public ReportResponseMapper(DailyReportRepository dailyReportRepository, ObjectMapper objectMapper) {
        this.dailyReportRepository = dailyReportRepository;
        this.objectMapper = objectMapper;
    }

    public DailyReportResponse toDailyResponse(DailyReport report) {
        return DailyReportResponse.builder()
                .reportDate(report.getReportDate())
                .score(report.getAiScore())
                .grade(report.getGrade())
                .summary(report.getSummary())
                .highlights(parseJson(report.getHighlights(), new TypeReference<List<String>>() {}, List.of()))
                .improvements(parseJson(report.getImprovements(), new TypeReference<List<String>>() {}, List.of()))
                .recommendations(parseJson(report.getRecommendations(), new TypeReference<List<String>>() {}, List.of()))
                .nutrientScores(parseJson(report.getNutrientScores(), new TypeReference<Map<String, Integer>>() {}, Map.of()))
                .riskFlags(parseJson(report.getRiskFlags(), new TypeReference<List<String>>() {}, List.of()))
                .metrics(parseJson(report.getMetrics(), new TypeReference<Map<String, Object>>() {}, Map.of()))
                .updatedAt(report.getUpdatedAt())
                .build();
    }

    public WeeklyReportResponse toWeeklyResponse(WeeklyReport report) {
        List<DailyScorePoint> scores = dailyReportRepository.findScoresByMemberAndRange(
                report.getMemberId(), report.getStartDate(), report.getEndDate());
        List<WeeklyDailyScoreResponse> dailyScores = scores.stream()
                .map(score -> WeeklyDailyScoreResponse.builder()
                        .date(score.getDate())
                        .score(score.getScore())
                        .build())
                .toList();
        Map<String, Object> trendMap = parseJson(report.getTrend(), new TypeReference<Map<String, Object>>() {}, null);
        if (trendMap == null || trendMap.isEmpty()) {
            trendMap = buildTrendMap(report);
        }
        return WeeklyReportResponse.builder()
                .startDate(report.getStartDate())
                .endDate(report.getEndDate())
                .score(report.getAiScore())
                .grade(report.getGrade())
                .summary(report.getSummary())
                .patternSummary(report.getPatternSummary())
                .bestDay(report.getBestDay())
                .bestReason(report.getBestReason())
                .worstDay(report.getWorstDay())
                .worstReason(report.getWorstReason())
                .nextWeekFocus(report.getNextWeekFocus())
                .highlights(parseJson(report.getHighlights(), new TypeReference<List<String>>() {}, List.of()))
                .improvements(parseJson(report.getImprovements(), new TypeReference<List<String>>() {}, List.of()))
                .recommendations(parseJson(report.getRecommendations(), new TypeReference<List<String>>() {}, List.of()))
                .trend(trendMap == null ? Map.of() : trendMap)
                .riskFlags(parseJson(report.getRiskFlags(), new TypeReference<List<String>>() {}, List.of()))
                .consistencyScore(report.getConsistencyScore())
                .dailyScores(dailyScores)
                .updatedAt(report.getUpdatedAt())
                .build();
    }

    private <T> T parseJson(String raw, TypeReference<T> type, T fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            T value = objectMapper.readValue(raw, type);
            return value == null ? fallback : value;
        } catch (Exception e) {
            return fallback;
        }
    }

    private Map<String, Object> buildTrendMap(WeeklyReport report) {
        Map<String, Object> trend = new java.util.HashMap<>();
        trend.put("patternSummary", report.getPatternSummary());
        trend.put("bestDay", report.getBestDay() != null ? report.getBestDay().toString() : null);
        trend.put("worstDay", report.getWorstDay() != null ? report.getWorstDay().toString() : null);
        trend.put("nextWeekFocus", report.getNextWeekFocus());
        return trend;
    }
}
