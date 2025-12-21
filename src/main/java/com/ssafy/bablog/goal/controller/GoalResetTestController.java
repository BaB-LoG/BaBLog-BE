package com.ssafy.bablog.goal.controller;

import com.ssafy.bablog.goal.service.GoalResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/reset")
public class GoalResetTestController {

    private final GoalResetService goalResetService;

    @PostMapping("/daily")
    public void resetDaily() {
        goalResetService.resetDailyGoals();
    }

    @PostMapping("/weekly")
    public void resetWeekly() {
        goalResetService.resetWeeklyGoals();
    }
}
