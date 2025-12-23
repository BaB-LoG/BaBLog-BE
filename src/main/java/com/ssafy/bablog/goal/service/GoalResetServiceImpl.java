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
        goalHistoryRepository.insertDailySnapshots(recordDate);
        goalRepository.resetDailyGoals();
    }

    @Override
    public void resetWeeklyGoals() {
        // 주간 목표 리셋 (progress 0으로)
        goalRepository.resetWeeklyGoals();

        // 새 주(오늘 월요일)를 위한 history 초기화
        LocalDate recordDate = LocalDate.now(); // 오늘이 월요일임
        goalHistoryRepository.insertWeeklySnapshots(recordDate);
    }

}
