package com.ssafy.bablog.report.repository.mapper;

import com.ssafy.bablog.report.domain.DailyReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface DailyReportMapper {
    void upsert(DailyReport report);

    DailyReport findByMemberAndDate(@Param("memberId") Long memberId,
                                    @Param("reportDate") LocalDate reportDate);
}
