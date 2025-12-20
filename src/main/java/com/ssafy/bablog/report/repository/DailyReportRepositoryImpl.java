package com.ssafy.bablog.report.repository;

import com.ssafy.bablog.report.domain.DailyReport;
import com.ssafy.bablog.report.repository.mapper.DailyReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DailyReportRepositoryImpl implements DailyReportRepository {

    private final DailyReportMapper mapper;

    @Override
    public void upsert(DailyReport report) {
        mapper.upsert(report);
    }

    @Override
    public Optional<DailyReport> findByMemberAndDate(Long memberId, LocalDate reportDate) {
        return Optional.ofNullable(mapper.findByMemberAndDate(memberId, reportDate));
    }
}
