package com.ssafy.bablog.report.repository;

import com.ssafy.bablog.report.domain.WeeklyReport;

import java.time.LocalDate;
import java.util.Optional;

public interface WeeklyReportRepository {
    void upsert(WeeklyReport report);

    Optional<WeeklyReport> findByMemberAndRange(Long memberId, LocalDate startDate, LocalDate endDate);
}
