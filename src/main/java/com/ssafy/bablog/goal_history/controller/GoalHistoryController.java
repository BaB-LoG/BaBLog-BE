package com.ssafy.bablog.goal_history.controller;

import com.ssafy.bablog.goal_history.domain.GoalHistory;
import com.ssafy.bablog.goal_history.dto.GoalHistoryResponse;
import com.ssafy.bablog.goal_history.dto.GoalHistoryUpdateRequest;
import com.ssafy.bablog.goal_history.service.GoalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goal-histories")
public class GoalHistoryController {

    private final GoalHistoryService goalHistoryService;

    // 달력 날짜 클릭 조회
    @GetMapping
    public ResponseEntity<List<GoalHistoryResponse>> getByDate(
            @RequestParam Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<GoalHistory> histories = goalHistoryService.getHistoriesByDate(memberId, date);

        List<GoalHistoryResponse> response = histories.stream()
                .map(GoalHistoryResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<com.ssafy.bablog.goal_history.dto.CalendarSummaryResponse>> getCalendarSummary(
            @RequestParam Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(goalHistoryService.getCalendarSummary(memberId, startDate, endDate));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<com.ssafy.bablog.goal_history.dto.GoalStatsResponse>> getMonthlyStats(
            @RequestParam Long memberId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(goalHistoryService.getMonthlyStats(memberId, year, month));
    }

    // 과거 기록 수정 (progress만)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long id,
            @RequestBody GoalHistoryUpdateRequest request) {
        goalHistoryService.updateProgress(id, request.getDelta());
        return ResponseEntity.ok().build();
    }

    // 과거 기록 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        goalHistoryService.deleteHistory(id);
        return ResponseEntity.noContent().build();
    }

    // 오늘의 요약 통계 조회
    @GetMapping("/summary/today")
    public ResponseEntity<com.ssafy.bablog.goal_history.dto.TodaySummaryResponse> getTodaySummary(
            @RequestParam Long memberId) {
        return ResponseEntity.ok(goalHistoryService.getTodaySummary(memberId));
    }

    // 베스트/워스트 목표 조회
    @GetMapping("/stats/highlights")
    public ResponseEntity<com.ssafy.bablog.goal_history.dto.BestWorstGoalResponse> getBestAndWorstGoals(
            @RequestParam Long memberId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(goalHistoryService.getBestAndWorstGoals(memberId, year, month));
    }
}
