package com.ssafy.bablog.report.repository;

import com.ssafy.bablog.report.domain.DailyReport;
import com.ssafy.bablog.report.service.dto.DailyScorePoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyReportRepository {
    void upsert(DailyReport report);

    Optional<DailyReport> findByMemberAndDate(Long memberId, LocalDate reportDate);

    List<DailyScorePoint> findScoresByMemberAndRange(Long memberId, LocalDate startDate, LocalDate endDate);
}
