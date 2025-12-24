package com.ssafy.bablog.report.controller;

import com.ssafy.bablog.report.dto.DailyReportResponse;
import com.ssafy.bablog.report.dto.WeeklyReportResponse;
import com.ssafy.bablog.report.domain.DailyReport;
import com.ssafy.bablog.report.domain.WeeklyReport;
import com.ssafy.bablog.report.service.ReportService;
import com.ssafy.bablog.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportResponseMapper responseMapper;

    @PostMapping("/daily")
    public ResponseEntity<DailyReportResponse> generateDaily(@AuthenticationPrincipal MemberPrincipal principal,
                                                             @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyReport report = reportService.generateDailyReport(principal.getId(), date);
        return ResponseEntity.ok(responseMapper.toDailyResponse(report));
    }

    @PostMapping("/weekly")
    public ResponseEntity<WeeklyReportResponse> generateWeekly(@AuthenticationPrincipal MemberPrincipal principal,
                                                               @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate startDate = reportService.weekStart(date);
        LocalDate endDate = reportService.weekEnd(date);
        WeeklyReport report = reportService.generateWeeklyReport(principal.getId(), startDate, endDate);
        return ResponseEntity.ok(responseMapper.toWeeklyResponse(report));
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyReportResponse> getDaily(@AuthenticationPrincipal MemberPrincipal principal,
                                                        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Optional<DailyReport> report = reportService.getDailyReport(principal.getId(), date);
        return report
                .map(value -> ResponseEntity.ok(responseMapper.toDailyResponse(value)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/weekly")
    public ResponseEntity<WeeklyReportResponse> getWeekly(@AuthenticationPrincipal MemberPrincipal principal,
                                                          @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Optional<WeeklyReport> report = reportService.getWeeklyReport(principal.getId(), date);
        return report
                .map(value -> ResponseEntity.ok(responseMapper.toWeeklyResponse(value)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
