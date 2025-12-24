package com.ssafy.bablog.goal_history.service;

import com.ssafy.bablog.goal_history.domain.GoalHistory;

import java.time.LocalDate;
import java.util.List;

public interface GoalHistoryService {

    // 달력 날짜 클릭 시 기록 조회
    List<GoalHistory> getHistoriesByDate(Long memberId, LocalDate date);

    // 과거 기록 수정 (progress만 가능)
    void updateProgress(Long historyId, Integer delta);

    // 과거 기록 삭제
    void deleteHistory(Long historyId);

    // 달력 시각화를 위한 요약 정보 조회
    List<com.ssafy.bablog.goal_history.dto.CalendarSummaryResponse> getCalendarSummary(Long memberId,
            LocalDate startDate, LocalDate endDate);
}
