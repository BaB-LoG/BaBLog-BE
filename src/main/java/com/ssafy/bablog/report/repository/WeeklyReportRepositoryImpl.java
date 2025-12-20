package com.ssafy.bablog.report.repository;

import com.ssafy.bablog.report.domain.WeeklyReport;
import com.ssafy.bablog.report.repository.mapper.WeeklyReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WeeklyReportRepositoryImpl implements WeeklyReportRepository {

    private final WeeklyReportMapper mapper;

    @Override
    public void upsert(WeeklyReport report) {
        mapper.upsert(report);
    }

    @Override
    public Optional<WeeklyReport> findByMemberAndRange(Long memberId, LocalDate startDate, LocalDate endDate) {
        return Optional.ofNullable(mapper.findByMemberAndRange(memberId, startDate, endDate));
    }
}
