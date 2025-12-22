package com.ssafy.bablog.report.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AiWeeklyReportResult {
    private Integer score;
    private String grade;
    private Integer consistencyScore;
    private String summary;
    private String patternSummary;
    private String bestDay;
    private String bestReason;
    private String worstDay;
    private String worstReason;
    private String nextWeekFocus;
    private List<String> highlights;
    private List<String> improvements;
    private List<String> recommendations;
    private List<String> riskFlags;
    private Map<String, Object> trend;
}
