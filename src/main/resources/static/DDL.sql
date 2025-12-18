CREATE TABLE `member` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(120) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `gender` enum('MALE','FEMALE') NOT NULL,
  `birth_date` date,
  `height_cm` decimal(5,2),
  `weight_kg` decimal(5,2),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `member_nutrient` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
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
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `member_nutrient_daily` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
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
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `meal_log` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
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
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `meal` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
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
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `food` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
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
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `meal_food` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `meal_id` bigint NOT NULL,
  `food_id` bigint NOT NULL,
  `quantity` decimal(10,2),
  `unit` varchar(50),
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `goal` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `goal_type` enum('DAILY','WEEKLY') NOT NULL,
  `title` varchar(100) NOT NULL,
  `target_value` decimal(10,2),
  `progress_value` decimal(10,2) DEFAULT 0,
  `start_date` date,
  `end_date` date,
  `click_per_progress` decimal(10,2),
  `is_completed` boolean DEFAULT false,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `daily_report` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `ai_score` integer,
  `report_date` date,
  `description` text,
  `positive_insight` text,
  `recommendation` text,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `weekly_report` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `ai_score` integer,
  `start_date` date,
  `end_date` date,
  `description` text,
  `positive_insight` text,
  `recommendation` text,
  `created_at` datetime DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime DEFAULT (CURRENT_TIMESTAMP)
);

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

CREATE INDEX `idx_daily_report_member_report_date` ON `daily_report` (`member_id`, `report_date`);

CREATE INDEX `idx_weekly_report_member_period` ON `weekly_report` (`member_id`, `start_date`, `end_date`);

ALTER TABLE `member_nutrient` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);

ALTER TABLE `meal_log` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);

ALTER TABLE `meal` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);

ALTER TABLE `meal_food` ADD FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`);

ALTER TABLE `meal_food` ADD FOREIGN KEY (`food_id`) REFERENCES `food` (`id`);

ALTER TABLE `meal_log` ADD FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`);

ALTER TABLE `goal` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);

ALTER TABLE `daily_report` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);

ALTER TABLE `weekly_report` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);
