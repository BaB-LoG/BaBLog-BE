package com.ssafy.bablog.report.repository;

import com.ssafy.bablog.report.domain.DailyReport;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyReportRepository {
    void upsert(DailyReport report);

    Optional<DailyReport> findByMemberAndDate(Long memberId, LocalDate reportDate);
}
