package com.ssafy.bablog.goal_history.service;

import com.ssafy.bablog.goal_history.domain.GoalHistory;
import com.ssafy.bablog.goal_history.repository.GoalHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalHistoryServiceImpl implements GoalHistoryService {

    private final GoalHistoryRepository goalHistoryRepository;

    // 달력 날짜 클릭 조회
    @Override
    public List<GoalHistory> getHistoriesByDate(Long memberId, LocalDate date) {
        return goalHistoryRepository.findByMemberAndDate(memberId, date);
    }

    // 과거 기록 수정 - progressValue 변경
    @Transactional
    @Override
    public void updateProgress(Long historyId, Integer delta) {

        GoalHistory history = goalHistoryRepository.findById(historyId);
        if (history == null) {
            throw new IllegalArgumentException("해당 목표 기록을 찾을 수 없습니다.");
        }

        BigDecimal click = history.getClickPerProgress();
        BigDecimal current = history.getProgressValue();

        BigDecimal newProgress =
                current.add(click.multiply(BigDecimal.valueOf(delta)));

        // 음수 방지
        if (newProgress.compareTo(BigDecimal.ZERO) < 0) {
            newProgress = BigDecimal.ZERO;
        }

        boolean isCompleted =
                history.getTargetValue() != null &&
                        newProgress.compareTo(history.getTargetValue()) >= 0;

        goalHistoryRepository.updateProgress(
                historyId,
                newProgress,
                isCompleted
        );
    }


    // 과거 기록 삭제
    @Transactional
    @Override
    public void deleteHistory(Long historyId) {
        goalHistoryRepository.deleteById(historyId);
    }
}
