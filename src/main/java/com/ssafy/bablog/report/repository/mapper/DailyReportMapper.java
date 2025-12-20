package com.ssafy.bablog.report.repository.mapper;

import com.ssafy.bablog.report.domain.DailyReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.bablog.report.service.dto.DailyScorePoint;

@Mapper
public interface DailyReportMapper {
    void upsert(DailyReport report);

    DailyReport findByMemberAndDate(@Param("memberId") Long memberId,
                                    @Param("reportDate") LocalDate reportDate);

    List<DailyScorePoint> findScoresByMemberAndRange(@Param("memberId") Long memberId,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);
}
