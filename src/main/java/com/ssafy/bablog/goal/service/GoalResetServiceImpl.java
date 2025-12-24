package com.ssafy.bablog.goal.service;

import com.ssafy.bablog.goal.repository.GoalRepository;
import com.ssafy.bablog.goal_history.repository.GoalHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GoalResetServiceImpl implements GoalResetService {

    private final GoalRepository goalRepository;
    private final GoalHistoryRepository goalHistoryRepository;

    @Transactional
    @Override
    public void resetDailyGoals() {
        LocalDate recordDate = LocalDate.now().minusDays(1);
        // 1. 현재 상태 기록
        goalHistoryRepository.insertDailySnapshots(recordDate);
        // 2. 목표 초기화
        goalRepository.resetDailyGoals(recordDate);
    }

    @Transactional
    @Override
    public void resetWeeklyGoals() {
        // 이번 주 월요일 계산
        LocalDate thisMonday = LocalDate.now().with(java.time.DayOfWeek.MONDAY);

        // 1. 이번 주 마무리 기록 (수시 업데이트 외 혹시 모를 누락 방지)
        goalHistoryRepository.insertWeeklySnapshots(thisMonday);

        // 2. 주간 목표 리셋 (progress 0으로)
        goalRepository.resetWeeklyGoals(thisMonday);

        // 3. 다음 주를 위한 history 초기화 (새로운 시작 준비)
        LocalDate nextMonday = thisMonday.plusWeeks(1);
        goalHistoryRepository.insertWeeklySnapshots(nextMonday);
    }

}
