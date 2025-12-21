package com.ssafy.bablog.goal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoalResetScheduler {

    private final GoalResetService goalResetService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetDaily() {
        goalResetService.resetDailyGoals();
    }

    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
    public void resetWeekly() {
        goalResetService.resetWeeklyGoals();
    }
}
