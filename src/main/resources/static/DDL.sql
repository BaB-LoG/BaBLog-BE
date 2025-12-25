CREATE TABLE `member` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `email` varchar(120) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `gender` enum('MALE','FEMALE') NOT NULL,
  `birth_date` date,
  `height_cm` decimal(5,2),
  `weight_kg` decimal(5,2),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `member_nutrient` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `member_id` bigint NOT NULL,
  `kcal` decimal(6,2),
  `protein` decimal(6,2),
  `fat` decimal(6,2),
  `saturated_fat` decimal(6,2),
  `trans_fat` decimal(6,2),
  `carbohydrates` decimal(6,2),
  `sugar` decimal(6,2),
  `natrium` decimal(8,2),
  `cholesterol` decimal(6,2),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_member_nutrient_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `member_nutrient_daily` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `member_id` bigint NOT NULL,
  `target_date` date NOT NULL,
  `kcal` decimal(6,2),
  `protein` decimal(6,2),
  `fat` decimal(6,2),
  `saturated_fat` decimal(6,2),
  `trans_fat` decimal(6,2),
  `carbohydrates` decimal(6,2),
  `sugar` decimal(6,2),
  `natrium` decimal(8,2),
  `cholesterol` decimal(6,2),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_member_nutrient_daily_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `meal` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `member_id` bigint NOT NULL,
  `meal_type` enum('BREAKFAST','LUNCH','DINNER','SNACK') NOT NULL,
  `meal_date` date NOT NULL,
  `kcal` decimal(6,2),
  `protein` decimal(6,2),
  `fat` decimal(6,2),
  `saturated_fat` decimal(6,2),
  `trans_fat` decimal(6,2),
  `carbohydrates` decimal(6,2),
  `sugar` decimal(6,2),
  `natrium` decimal(8,2),
  `cholesterol` decimal(6,2),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_meal_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `meal_log` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `meal_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `logged_at` datetime,
  `kcal` decimal(6,2),
  `protein` decimal(6,2),
  `fat` decimal(6,2),
  `saturated_fat` decimal(6,2),
  `trans_fat` decimal(6,2),
  `carbohydrates` decimal(6,2),
  `sugar` decimal(6,2),
  `natrium` decimal(8,2),
  `cholesterol` decimal(6,2),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_meal_log_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
  CONSTRAINT `fk_meal_log_meal` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `food` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `standard` decimal(10,2),
  `name` varchar(255),
  `kcal` decimal(6,2),
  `protein` decimal(6,2),
  `fat` decimal(6,2),
  `saturated_fat` decimal(6,2),
  `trans_fat` decimal(6,2),
  `carbohydrates` decimal(6,2),
  `sugar` decimal(6,2),
  `natrium` decimal(8,2),
  `cholesterol` decimal(6,2),
  `food_weight` decimal(6,2),
  `vendor` varchar(255),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `meal_food` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `meal_id` bigint NOT NULL,
  `food_id` bigint NOT NULL,
  `intake` decimal(10,2),
  `unit` varchar(50),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_meal_food_meal` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`),
  CONSTRAINT `fk_meal_food_food` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `goal` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `member_id` bigint NOT NULL,
  `goal_type` enum('DAILY','WEEKLY') NOT NULL,
  `title` varchar(100) NOT NULL,
  `target_value` decimal(10,2),
  `progress_value` decimal(10,2) DEFAULT 0,
  `start_date` date,
  `end_date` date,
  `click_per_progress` decimal(10,2),
  `is_completed` tinyint(1) DEFAULT 0,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_goal_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `goal_history` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `goal_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `goal_type` enum('DAILY','WEEKLY') NOT NULL,
  `title` varchar(100) NOT NULL,
  `target_value` decimal(10,2),
  `progress_value` decimal(10,2),
  `click_per_progress` decimal(10,2),
  `is_completed` tinyint(1) DEFAULT 0,
  `record_date` date NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `uk_goal_record` UNIQUE (`goal_id`, `record_date`),
  INDEX `idx_member_record` (`member_id`, `record_date`),
  CONSTRAINT `fk_goal_history_goal` FOREIGN KEY (`goal_id`) REFERENCES `goal`(`id`),
  CONSTRAINT `fk_goal_history_member` FOREIGN KEY (`member_id`) REFERENCES `member`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `daily_report` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `member_id` bigint NOT NULL,
  `report_date` date NOT NULL,
  `ai_score` int,
  `grade` varchar(20),
  `summary` text,
  `highlights` text,
  `improvements` text,
  `recommendations` text,
  `nutrient_scores` text,
  `risk_flags` text,
  `metrics` text,
  `report_version` varchar(20),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_daily_report_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `weekly_report` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `member_id` bigint NOT NULL,
  `ai_score` int,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `grade` varchar(20),
  `summary` text,
  `pattern_summary` text,
  `best_day` date,
  `best_reason` text,
  `worst_day` date,
  `worst_reason` text,
  `next_week_focus` text,
  `highlights` text,
  `improvements` text,
  `recommendations` text,
  `trend` text,
  `risk_flags` text,
  `consistency_score` int,
  `report_version` varchar(20),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_weekly_report_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `batch_failure_log` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `job_name` varchar(100) NOT NULL,
  `step_name` varchar(100) NOT NULL,
  `member_id` bigint,
  `target_date` date,
  `week_start` date,
  `week_end` date,
  `error_class` varchar(200) NOT NULL,
  `error_message` varchar(1000),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE UNIQUE INDEX `idx_member_email` ON `member` (`email`);
CREATE INDEX `idx_meal_log_member_logged_at` ON `meal_log` (`member_id`, `logged_at`);
CREATE INDEX `idx_meal_log_logged_at` ON `meal_log` (`logged_at`);
CREATE UNIQUE INDEX `idx_meal_log_meal` ON `meal_log` (`meal_id`);
CREATE INDEX `idx_meal_member` ON `meal` (`member_id`);
CREATE UNIQUE INDEX `idx_meal_member_date_type` ON `meal` (`member_id`, `meal_date`, `meal_type`);
CREATE INDEX `idx_meal_member_type` ON `meal` (`member_id`, `meal_type`);
CREATE INDEX `idx_food_name` ON `food` (`name`);
CREATE UNIQUE INDEX `idx_meal_food_meal_food` ON `meal_food` (`meal_id`, `food_id`);
CREATE INDEX `idx_meal_food_food_id` ON `meal_food` (`food_id`);
CREATE INDEX `idx_goal_member_type` ON `goal` (`member_id`, `goal_type`);
CREATE INDEX `idx_goal_member_period` ON `goal` (`member_id`, `start_date`, `end_date`);
CREATE UNIQUE INDEX `idx_daily_report_member_report_date` ON `daily_report` (`member_id`, `report_date`);
CREATE UNIQUE INDEX `idx_weekly_report_member_period` ON `weekly_report` (`member_id`, `start_date`, `end_date`);
CREATE UNIQUE INDEX `idx_member_nutrient_daily_member_date` ON `member_nutrient_daily` (`member_id`, `target_date`);
