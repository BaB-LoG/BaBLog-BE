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
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        List<GoalHistory> histories =
                goalHistoryService.getHistoriesByDate(memberId, date);

        List<GoalHistoryResponse> response = histories.stream()
                .map(GoalHistoryResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    //과거 기록 수정 (progress만)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long id,
            @RequestBody GoalHistoryUpdateRequest request
    ) {
        goalHistoryService.updateProgress(id, request.getDelta());
        return ResponseEntity.ok().build();
    }


    //과거 기록 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        goalHistoryService.deleteHistory(id);
        return ResponseEntity.noContent().build();
    }
}
