package com.ssafy.bablog.goal.service;

import com.ssafy.bablog.goal.repository.GoalRepository;
import com.ssafy.bablog.goal_history.repository.GoalHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
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
        LocalDate recordDate =
                LocalDate.now().minusWeeks(1).with(DayOfWeek.SUNDAY);

        goalHistoryRepository.insertWeeklySnapshots(recordDate);
        goalRepository.resetWeeklyGoals();
    }



}
