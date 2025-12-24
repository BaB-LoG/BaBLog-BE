package com.ssafy.bablog.goal_history.repository;

import com.ssafy.bablog.goal_history.domain.GoalHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface GoalHistoryRepository {

        // DAILY 목표 스냅샷 저장
        int insertDailySnapshots(@Param("recordDate") LocalDate recordDate);

        // WEEKLY 목표 스냅샷 저장
        int insertWeeklySnapshots(@Param("recordDate") LocalDate recordDate);

        // 단일 WEEKLY 목표 스냅샷 저장 (최초 생성 시)
        int insertWeeklySnapshotsForGoal(
                        @Param("goalId") Long goalId,
                        @Param("recordDate") LocalDate recordDate);

        // // goal 종료일 변경 시 history 동기화
        // int updateEndDateByGoalId(
        // @Param("goalId") Long goalId,
        // @Param("endDate") LocalDate endDate
        // );

        // 달력으로 과거 기록 조회
        List<GoalHistory> findByMemberAndDate(
                        @Param("memberId") Long memberId,
                        @Param("date") LocalDate date);

        // 특정 목표의 특정 날짜 기록 조회
        GoalHistory findByGoalIdAndRecordDate(
                        @Param("goalId") Long goalId,
                        @Param("recordDate") LocalDate recordDate);

        // 수정을 위한 단건 조회
        GoalHistory findById(@Param("id") Long id);

        // 과거 기록 수정 (progress만)

        int updateProgress(
                        @Param("id") Long id,
                        @Param("progressValue") BigDecimal progressValue,
                        @Param("isCompleted") Boolean isCompleted);

        // goalId와 recordDate로 기록 수정
        int updateProgressByGoalIdAndRecordDate(
                        @Param("goalId") Long goalId,
                        @Param("recordDate") LocalDate recordDate,
                        @Param("progressValue") BigDecimal progressValue,
                        @Param("isCompleted") Boolean isCompleted);

        // goalId와 recordDate로 전체 속성 수정 (동기화용)
        int updateWeeklyHistoryAttributes(
                        @Param("goalId") Long goalId,
                        @Param("recordDate") LocalDate recordDate,
                        @Param("title") String title,
                        @Param("targetValue") BigDecimal targetValue,
                        @Param("progressValue") BigDecimal progressValue,
                        @Param("isCompleted") Boolean isCompleted);

        // 기록 삭제

        int deleteById(@Param("id") Long id);

        // 특정 목표에 대한 모든 기록 삭제
        int deleteByGoalId(@Param("goalId") Long goalId);

}
