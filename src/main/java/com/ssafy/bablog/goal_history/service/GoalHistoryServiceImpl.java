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
    private final com.ssafy.bablog.goal.repository.GoalRepository goalRepository;

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

        BigDecimal newProgress = current.add(click.multiply(BigDecimal.valueOf(delta)));

        // 음수 방지
        if (newProgress.compareTo(BigDecimal.ZERO) < 0) {
            newProgress = BigDecimal.ZERO;
        }

        boolean isCompleted = history.getTargetValue() != null &&
                newProgress.compareTo(history.getTargetValue()) >= 0;

        goalHistoryRepository.updateProgress(
                historyId,
                newProgress,
                isCompleted);
    }

    // 과거 기록 삭제
    @Transactional
    @Override
    public void deleteHistory(Long historyId) {
        goalHistoryRepository.deleteById(historyId);
    }

    @Override
    public List<com.ssafy.bablog.goal_history.dto.CalendarSummaryResponse> getCalendarSummary(Long memberId,
            LocalDate startDate, LocalDate endDate) {
        List<GoalHistory> histories = goalHistoryRepository.findByMemberIdAndDateRange(memberId, startDate, endDate);

        LocalDate today = LocalDate.now();

        // Group by date
        java.util.Map<LocalDate, java.util.List<GoalHistory>> historyMap = histories.stream()
                .collect(java.util.stream.Collectors.groupingBy(GoalHistory::getRecordDate));

        java.util.List<com.ssafy.bablog.goal_history.dto.CalendarSummaryResponse> result = new java.util.ArrayList<>();

        // If range include today, we need to handle today separately or merge current
        // goals
        // But GoalHistory already has snapshots of TODAY if they were saved?
        // No, snapshots are usually saved at midnight or when updated.
        // Actually, our system might not have today's snapshot in history table yet if
        // it only saves at reset.
        // Let's fetch current goals from goal table.
        List<com.ssafy.bablog.goal.domain.Goal> currentDailyGoals = goalRepository.findByMemberIdAndGoalType(memberId,
                com.ssafy.bablog.goal.domain.GoalType.DAILY);
        List<com.ssafy.bablog.goal.domain.Goal> currentWeeklyGoals = goalRepository.findByMemberIdAndGoalType(memberId,
                com.ssafy.bablog.goal.domain.GoalType.WEEKLY);

        // Iterate through dates in range
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            java.util.List<GoalHistory> dateHistories = historyMap.getOrDefault(date, new java.util.ArrayList<>());

            int dailyTotal = 0;
            int dailyCompleted = 0;
            boolean hasDailyProgress = false;
            String weeklyStatus = "NONE";

            // Weekly Logic refined
            LocalDate weekMonday = date.minusDays(date.getDayOfWeek().getValue() - 1);
            LocalDate currentMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);

            if (weekMonday.equals(currentMonday)) {
                // CURRENT WEEK: Use current weekly goals
                if (!currentWeeklyGoals.isEmpty()) {
                    boolean allDone = currentWeeklyGoals.stream().allMatch(g -> g.isCompleted());
                    boolean someProgress = currentWeeklyGoals.stream()
                            .anyMatch(g -> g.getProgressValue().compareTo(BigDecimal.ZERO) > 0);
                    weeklyStatus = allDone ? "COMPLETED" : (someProgress ? "IN_PROGRESS" : "NONE");
                }
            } else if (weekMonday.isBefore(currentMonday)) {
                // PAST WEEK: Use historical records for that week's Monday
                java.util.List<GoalHistory> weekHistories = historyMap.getOrDefault(weekMonday,
                        new java.util.ArrayList<>());
                List<GoalHistory> weeklies = weekHistories.stream()
                        .filter(h -> h.getGoalType() == com.ssafy.bablog.goal.domain.GoalType.WEEKLY)
                        .collect(java.util.stream.Collectors.toList());

                if (!weeklies.isEmpty()) {
                    boolean allDone = weeklies.stream().allMatch(h -> h.getIsCompleted());
                    // Only COMPLETED is shown for past weeks, no IN_PROGRESS
                    weeklyStatus = allDone ? "COMPLETED" : "NONE";
                }
            } else {
                // FUTURE WEEK: No dots
                weeklyStatus = "NONE";
            }

            if (date.equals(today)) {
                // Use current goals for today (Daily part)
                dailyTotal = currentDailyGoals.size();
                dailyCompleted = (int) currentDailyGoals.stream().filter(g -> g.isCompleted()).count();
                hasDailyProgress = currentDailyGoals.stream()
                        .anyMatch(g -> g.getProgressValue().compareTo(BigDecimal.ZERO) > 0);
            } else if (date.isBefore(today)) {
                // Use histories for past dates (Daily part)
                List<GoalHistory> dailies = dateHistories.stream()
                        .filter(h -> h.getGoalType() == com.ssafy.bablog.goal.domain.GoalType.DAILY)
                        .collect(java.util.stream.Collectors.toList());
                dailyTotal = dailies.size();
                dailyCompleted = (int) dailies.stream().filter(h -> h.getIsCompleted()).count();
                hasDailyProgress = dailies.stream().anyMatch(h -> h.getProgressValue().compareTo(BigDecimal.ZERO) > 0);
            }

            // Only add if there's any data
            if (dailyTotal > 0 || !weeklyStatus.equals("NONE")) {
                result.add(com.ssafy.bablog.goal_history.dto.CalendarSummaryResponse.builder()
                        .date(date)
                        .dailyTotal(dailyTotal)
                        .dailyCompleted(dailyCompleted)
                        .hasDailyProgress(hasDailyProgress)
                        .weeklyStatus(weeklyStatus)
                        .build());
            }
        }

        return result;
    }

    @Override
    public List<com.ssafy.bablog.goal_history.dto.GoalStatsResponse> getMonthlyStats(Long memberId, int year,
            int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // We use findByMemberIdAndDateRange but widen the start date slightly for
        // weekly
        // goals
        // to catch the Monday record if the month starts in the middle of a week.
        LocalDate queryStartDate = startDate.isBefore(startDate.minusDays(startDate.getDayOfWeek().getValue() - 1))
                ? startDate
                : startDate.minusDays(startDate.getDayOfWeek().getValue() - 1);

        List<GoalHistory> histories = goalHistoryRepository.findByMemberIdAndDateRange(memberId, queryStartDate,
                endDate);

        // Fetch current goals to include today's live data if it's the current month
        LocalDate today = LocalDate.now();
        boolean isCurrentMonth = (year == today.getYear() && month == today.getMonthValue());
        List<com.ssafy.bablog.goal.domain.Goal> currentGoals = isCurrentMonth
                ? goalRepository.findByMemberIdAndGoalType(memberId, null)
                : new java.util.ArrayList<>();

        // Group by goalId
        java.util.Map<Long, java.util.List<GoalHistory>> goalGroupMap = histories.stream()
                .collect(java.util.stream.Collectors.groupingBy(GoalHistory::getGoalId));

        // If it's the current month, ensure all current goals are in the map
        // and add a "virtual" history record for today/this week
        if (isCurrentMonth) {
            for (com.ssafy.bablog.goal.domain.Goal g : currentGoals) {
                java.util.List<GoalHistory> gHistories = goalGroupMap.computeIfAbsent(g.getId(),
                        k -> new java.util.ArrayList<>());

                // Add current status as a virtual history record
                GoalHistory virtualToday = GoalHistory.builder()
                        .goalId(g.getId())
                        .memberId(memberId)
                        .title(g.getTitle())
                        .goalType(g.getGoalType())
                        .progressValue(g.getProgressValue())
                        .targetValue(g.getTargetValue())
                        .isCompleted(g.isCompleted())
                        // For daily, today. For weekly, this week's Monday.
                        .recordDate(g.getGoalType() == com.ssafy.bablog.goal.domain.GoalType.DAILY ? today
                                : today.minusDays(today.getDayOfWeek().getValue() - 1))
                        .build();

                // Avoid double counting if a history record for today already exists (e.g. just
                // saved)
                boolean alreadyExists = gHistories.stream()
                        .anyMatch(h -> h.getRecordDate().equals(virtualToday.getRecordDate()));
                if (!alreadyExists) {
                    gHistories.add(virtualToday);
                }
            }
        }

        java.util.List<com.ssafy.bablog.goal_history.dto.GoalStatsResponse> stats = new java.util.ArrayList<>();

        for (java.util.Map.Entry<Long, java.util.List<GoalHistory>> entry : goalGroupMap.entrySet()) {
            List<GoalHistory> goalHistories = entry.getValue().stream()
                    .sorted(java.util.Comparator.comparing(GoalHistory::getRecordDate))
                    .collect(java.util.stream.Collectors.toList());

            if (goalHistories.isEmpty())
                continue;

            String title = goalHistories.get(0).getTitle();
            com.ssafy.bablog.goal.domain.GoalType type = goalHistories.get(0).getGoalType();

            // Calculate Max Streak
            int maxStreak = 0;
            int currentStreak = 0;
            for (GoalHistory h : goalHistories) {
                if (h.getIsCompleted() != null && h.getIsCompleted()) {
                    currentStreak++;
                    maxStreak = Math.max(maxStreak, currentStreak);
                } else {
                    currentStreak = 0;
                }
            }

            // Calculate Average Achievement Rate
            // We only consider records within the requested month for average
            List<GoalHistory> monthRecords = goalHistories.stream()
                    .filter(h -> h.getRecordDate() != null && !h.getRecordDate().isBefore(startDate)
                            && !h.getRecordDate().isAfter(endDate))
                    .collect(java.util.stream.Collectors.toList());

            double avgRate = 0;
            if (!monthRecords.isEmpty()) {
                double totalRate = 0;
                for (GoalHistory h : monthRecords) {
                    if (h.getTargetValue() != null && h.getTargetValue().compareTo(BigDecimal.ZERO) > 0) {
                        double progress = h.getProgressValue() != null ? h.getProgressValue().doubleValue() : 0;
                        double target = h.getTargetValue().doubleValue();
                        double rate = progress / target;
                        totalRate += Math.min(rate, 1.0);
                    }
                }
                avgRate = totalRate / monthRecords.size();
            }

            stats.add(com.ssafy.bablog.goal_history.dto.GoalStatsResponse.builder()
                    .goalId(entry.getKey())
                    .title(title)
                    .maxStreak(maxStreak)
                    .avgAchievementRate(avgRate * 100) // Convert to percentage
                    .goalType(type)
                    .build());
        }

        return stats;
    }
}
