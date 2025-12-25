-- Load test seed data for 2025-12-15..2025-12-21 (MySQL 5.7+)
-- Targets BaBLog_Test_DB and creates 1000 members with one week of meals.

USE BaBLog_Test_DB;

-- 0) Disable safe updates for this session (optional)
SET SQL_SAFE_UPDATES = 0;

-- 2) Prepare helper numbers (1..1000)
DROP TEMPORARY TABLE IF EXISTS tmp_numbers;
CREATE TEMPORARY TABLE tmp_numbers (n int NOT NULL PRIMARY KEY);

INSERT INTO tmp_numbers (n)
SELECT a.n + b.n * 10 + c.n * 100 + 1 AS n
FROM (
  SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
) a
CROSS JOIN (
  SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
) b
CROSS JOIN (
  SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
) c
WHERE (a.n + b.n * 10 + c.n * 100 + 1) <= 1000;

-- 3) Insert 1000 members (loadtest_user_1..1000)
INSERT IGNORE INTO member (
  email, password, name, gender, birth_date, height_cm, weight_kg
)
SELECT
  CONCAT('loadtest_user_', n, '@example.com'),
  'dummy-password',
  CONCAT('LoadTest', n),
  CASE WHEN MOD(n, 2) = 0 THEN 'MALE' ELSE 'FEMALE' END,
  '1990-01-01',
  170.00,
  70.00
FROM tmp_numbers;

-- 4) Insert member nutrient targets
INSERT INTO member_nutrient (
  member_id, kcal, protein, fat, saturated_fat, trans_fat, carbohydrates, sugar, natrium, cholesterol
)
SELECT
  m.id, 2200, 120, 70, 20, 1, 300, 50, 2000, 300
FROM member m
LEFT JOIN member_nutrient mn ON mn.member_id = m.id
WHERE m.email LIKE 'loadtest_user_%' AND mn.id IS NULL;

-- 5) Prepare 7 dates and meal types
DROP TEMPORARY TABLE IF EXISTS tmp_dates;
CREATE TEMPORARY TABLE tmp_dates (d date NOT NULL PRIMARY KEY);
INSERT INTO tmp_dates (d)
VALUES
  ('2025-12-15'), ('2025-12-16'), ('2025-12-17'),
  ('2025-12-18'), ('2025-12-19'), ('2025-12-20'),
  ('2025-12-21');

DROP TEMPORARY TABLE IF EXISTS tmp_meal_types;
CREATE TEMPORARY TABLE tmp_meal_types (meal_type varchar(20) NOT NULL PRIMARY KEY);
INSERT INTO tmp_meal_types (meal_type)
VALUES ('BREAKFAST'), ('LUNCH'), ('DINNER'), ('SNACK');

-- 5-1) Insert member_nutrient_daily snapshots
INSERT INTO member_nutrient_daily (
  member_id, target_date,
  kcal, protein, fat, saturated_fat, trans_fat,
  carbohydrates, sugar, natrium, cholesterol
)
SELECT
  mn.member_id,
  d.d,
  mn.kcal, mn.protein, mn.fat, mn.saturated_fat, mn.trans_fat,
  mn.carbohydrates, mn.sugar, mn.natrium, mn.cholesterol
FROM member_nutrient mn
JOIN member mem ON mem.id = mn.member_id
JOIN tmp_dates d
LEFT JOIN member_nutrient_daily mnd
  ON mnd.member_id = mn.member_id AND mnd.target_date = d.d
WHERE mem.email LIKE 'loadtest_user_%'
  AND mnd.id IS NULL;

-- 6) Insert meals for 7 days and 4 meal types
INSERT INTO meal (member_id, meal_type, meal_date)
SELECT m.id, t.meal_type, d.d
FROM member m
JOIN tmp_dates d
JOIN tmp_meal_types t
LEFT JOIN meal existing
  ON existing.member_id = m.id
 AND existing.meal_date = d.d
 AND existing.meal_type = t.meal_type
WHERE m.email LIKE 'loadtest_user_%'
  AND existing.id IS NULL;

-- 7) Prepare food list with row numbers
DROP TEMPORARY TABLE IF EXISTS tmp_food_seq;
CREATE TEMPORARY TABLE tmp_food_seq (rn int NOT NULL PRIMARY KEY, food_id bigint NOT NULL);

SET @rownum := 0;
INSERT INTO tmp_food_seq (rn, food_id)
SELECT (@rownum := @rownum + 1) AS rn, f.id
FROM food f
ORDER BY f.id;

SET @food_count := (SELECT COUNT(*) FROM tmp_food_seq);

-- 8) Insert meal_food (2 foods per meal)
INSERT INTO meal_food (meal_id, food_id, intake, unit)
SELECT m.id,
       f.food_id,
       100 + (m.id % 3) * 50,
       'g'
FROM meal m
JOIN member mem ON mem.id = m.member_id
JOIN tmp_food_seq f
  ON f.rn IN ((m.id % @food_count) + 1, ((m.id + 1) % @food_count) + 1)
LEFT JOIN meal_food mf
  ON mf.meal_id = m.id AND mf.food_id = f.food_id
WHERE mem.email LIKE 'loadtest_user_%'
  AND m.meal_date BETWEEN '2025-12-15' AND '2025-12-21'
  AND mf.id IS NULL;

-- 9) Update meal nutrient totals based on meal_food
UPDATE meal m
JOIN (
  SELECT
    mf.meal_id,
    SUM(f.kcal * (mf.intake / NULLIF(f.standard, 0))) AS kcal,
    SUM(f.protein * (mf.intake / NULLIF(f.standard, 0))) AS protein,
    SUM(f.fat * (mf.intake / NULLIF(f.standard, 0))) AS fat,
    SUM(f.saturated_fat * (mf.intake / NULLIF(f.standard, 0))) AS saturated_fat,
    SUM(f.trans_fat * (mf.intake / NULLIF(f.standard, 0))) AS trans_fat,
    SUM(f.carbohydrates * (mf.intake / NULLIF(f.standard, 0))) AS carbohydrates,
    SUM(f.sugar * (mf.intake / NULLIF(f.standard, 0))) AS sugar,
    SUM(f.natrium * (mf.intake / NULLIF(f.standard, 0))) AS natrium,
    SUM(f.cholesterol * (mf.intake / NULLIF(f.standard, 0))) AS cholesterol
  FROM meal_food mf
  JOIN food f ON f.id = mf.food_id
  GROUP BY mf.meal_id
) agg ON agg.meal_id = m.id
SET
  m.kcal = agg.kcal,
  m.protein = agg.protein,
  m.fat = agg.fat,
  m.saturated_fat = agg.saturated_fat,
  m.trans_fat = agg.trans_fat,
  m.carbohydrates = agg.carbohydrates,
  m.sugar = agg.sugar,
  m.natrium = agg.natrium,
  m.cholesterol = agg.cholesterol
WHERE m.meal_date BETWEEN '2025-12-15' AND '2025-12-21';

-- 10) Insert meal_log from meal totals
INSERT INTO meal_log (
  meal_id, member_id, logged_at,
  kcal, protein, fat, saturated_fat, trans_fat, carbohydrates, sugar, natrium, cholesterol
)
SELECT
  m.id,
  m.member_id,
  CONCAT(m.meal_date, ' 12:00:00'),
  m.kcal, m.protein, m.fat, m.saturated_fat, m.trans_fat,
  m.carbohydrates, m.sugar, m.natrium, m.cholesterol
FROM meal m
LEFT JOIN meal_log ml ON ml.meal_id = m.id
JOIN member mem ON mem.id = m.member_id
WHERE mem.email LIKE 'loadtest_user_%'
  AND m.meal_date BETWEEN '2025-12-15' AND '2025-12-21'
  AND ml.id IS NULL;
