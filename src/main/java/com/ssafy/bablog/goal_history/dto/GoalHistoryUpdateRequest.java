package com.ssafy.bablog.goal_history.dto;

import lombok.Getter;

@Getter
public class GoalHistoryUpdateRequest {

    /**
     * +1 / -1 같은 개념
     * 실제 증감량 = delta * clickPerProgress
     */
    private Integer delta;
}
