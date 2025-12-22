package com.ssafy.bablog.report.service;

import com.ssafy.bablog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private final MemberRepository memberRepository;
    private final ReportService reportService;

    @Scheduled(cron = "0 0 0 * * *")
    public void generateDailyReports() {
        LocalDate targetDate = LocalDate.now().minusDays(1);
        for (Long memberId : memberRepository.findAllIds()) {
            reportService.generateDailyReport(memberId, targetDate);
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void generateWeeklyReports() {
        LocalDate weekStart = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);
        for (Long memberId : memberRepository.findAllIds()) {
            reportService.generateWeeklyReport(memberId, weekStart, weekEnd);
        }
    }
}
