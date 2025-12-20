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
public class DailyReport {
    private Long id;
    private Long memberId;
    private LocalDate reportDate;
    private Integer aiScore;
    private String grade;
    private String summary;
    private String highlights;
    private String improvements;
    private String recommendations;
    private String nutrientScores;
    private String riskFlags;
    private String metrics;
    private String reportVersion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
