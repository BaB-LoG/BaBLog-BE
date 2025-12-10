package com.ssafy.bablog.meal.service;

import com.ssafy.bablog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class MealScheduler {

    private final MemberRepository memberRepository;
    private final MealService mealService;

    /**
     * 매일 정오에 사용자별 기본 식사(BREAKFAST, LUNCH, DINNER, SNACK)를 생성
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void createDailyMeals() {
        LocalDate today = LocalDate.now();
        for (Long memberId : memberRepository.findAllIds()) {
            mealService.createDailyMeals(memberId, today);
        }
        log.info("Daily meals created for {}", today);
    }
}
