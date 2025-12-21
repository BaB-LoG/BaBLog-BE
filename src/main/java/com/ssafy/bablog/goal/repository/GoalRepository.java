package com.ssafy.bablog.goal.repository;

import com.ssafy.bablog.goal.domain.Goal;
import com.ssafy.bablog.goal.domain.GoalType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GoalRepository {

    // 목표 등록
    int insertGoal(Goal goal);

    // 목표 목록 조회 (회원 + 타입)
    List<Goal> findByMemberIdAndGoalType(
            @Param("memberId") Long memberId,
            @Param("goalType") GoalType goalType
    );

    // 목표 상세 조회
    Optional<Goal> findByIdAndMemberId(
            @Param("goalId") Long goalId,
            @Param("memberId") Long memberId
    );

    // 목표 수정
    int updateGoal(Goal goal);

    // 목표 삭제
    int deleteByIdAndMemberId(
            @Param("goalId") Long goalId,
            @Param("memberId") Long memberId
    );

    // 목표 진행량 증가
    int updateProgress(Goal goal);


    // 목표 초기화
    int resetDailyGoals();
    int resetWeeklyGoals();




}
