package com.ssafy.bablog.report.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyReport {
    private Long id;
    private Long memberId;
    private Integer aiScore;
    private LocalDate startDate;
    private LocalDate endDate;
    private String grade;
    private String summary;
    private String patternSummary;
    private LocalDate bestDay;
    private String bestReason;
    private LocalDate worstDay;
    private String worstReason;
    private String nextWeekFocus;
    private String highlights;
    private String improvements;
    private String recommendations;
    private String trend;
    private String riskFlags;
    private Integer consistencyScore;
    private String reportVersion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
