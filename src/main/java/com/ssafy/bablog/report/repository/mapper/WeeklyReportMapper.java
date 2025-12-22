package com.ssafy.bablog.report.repository.mapper;

import com.ssafy.bablog.report.domain.WeeklyReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface WeeklyReportMapper {
    void upsert(WeeklyReport report);

    WeeklyReport findByMemberAndRange(@Param("memberId") Long memberId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}
