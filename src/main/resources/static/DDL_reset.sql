use BaBLog_DB;
-- 전부 DROP 후 재생성하기
SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------
-- 1) 앱 테이블 DROP
-- -------------------------
DROP TABLE IF EXISTS
    `goal_history`,
    `meal_food`,
    `meal_log`,
    `goal`,
    `weekly_report`,
    `daily_report`,
    `member_nutrient_daily`,
    `member_nutrient`,
    `meal`,
    `food`,
    `batch_failure_log`,
    `member`;

-- -------------------------
-- 2) Spring Batch 테이블 DROP
-- (FK 체인 때문에 순서 중요하지만, FK_CHECKS=0이면 크게 상관 없음)
-- -------------------------
DROP TABLE IF EXISTS
    `BATCH_STEP_EXECUTION_CONTEXT`,
    `BATCH_JOB_EXECUTION_CONTEXT`,
    `BATCH_STEP_EXECUTION`,
    `BATCH_JOB_EXECUTION_PARAMS`,
    `BATCH_JOB_EXECUTION`,
    `BATCH_JOB_INSTANCE`,
    `BATCH_STEP_EXECUTION_SEQ`,
    `BATCH_JOB_EXECUTION_SEQ`,
    `BATCH_JOB_SEQ`;

SET FOREIGN_KEY_CHECKS = 1;
