package com.ssafy.bablog.report.repository;

import com.ssafy.bablog.report.domain.DailyReport;
import com.ssafy.bablog.report.repository.mapper.DailyReportMapper;
import com.ssafy.bablog.report.service.dto.DailyScorePoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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

    @Override
    public List<DailyScorePoint> findScoresByMemberAndRange(Long memberId, LocalDate startDate, LocalDate endDate) {
        return mapper.findScoresByMemberAndRange(memberId, startDate, endDate);
    }
}
