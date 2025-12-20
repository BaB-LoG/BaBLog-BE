package com.ssafy.bablog.report.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.bablog.meal.controller.dto.MealWithFoodsResponse;
import com.ssafy.bablog.meal.service.MealService;
import com.ssafy.bablog.meal_log.repository.MealLogRepository;
import com.ssafy.bablog.meal_log.repository.mapper.MealLogDailyTotal;
import com.ssafy.bablog.member.domain.Member;
import com.ssafy.bablog.member.repository.MemberRepository;
import com.ssafy.bablog.member_nutrient.domain.MemberNutrientDaily;
import com.ssafy.bablog.member_nutrient.service.MemberNutrientService;
import com.ssafy.bablog.report.domain.DailyReport;
import com.ssafy.bablog.report.domain.WeeklyReport;
import com.ssafy.bablog.report.repository.DailyReportRepository;
import com.ssafy.bablog.report.repository.WeeklyReportRepository;
import com.ssafy.bablog.report.controller.dto.DailyReportResponse;
import com.ssafy.bablog.report.controller.dto.WeeklyReportResponse;
import com.ssafy.bablog.report.controller.dto.WeeklyDailyScoreResponse;
import com.ssafy.bablog.report.service.dto.AiDailyReportResult;
import com.ssafy.bablog.report.service.dto.AiWeeklyReportResult;
import com.ssafy.bablog.report.service.dto.DailyScorePoint;
import com.ssafy.bablog.report.service.dto.NutritionSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private static final String REPORT_VERSION = "v1";

    private final MemberRepository memberRepository;
    private final MemberNutrientService memberNutrientService;
    private final MealLogRepository mealLogRepository;
    private final MealService mealService;
    private final DailyReportRepository dailyReportRepository;
    private final WeeklyReportRepository weeklyReportRepository;
    private final ReportAiService reportAiService;
    private final ObjectMapper objectMapper;

    @Transactional
    public DailyReport generateDailyReport(Long memberId, LocalDate date) {
        Member member = getMember(memberId);
        MemberNutrientDaily target = memberNutrientService.getDaily(memberId, date);
        MealLogDailyTotal dailyTotal = mealLogRepository.findDailyTotal(memberId, date);
        NutritionSnapshot actual = NutritionSnapshot.from(dailyTotal);
        List<MealWithFoodsResponse> meals = mealService.getMeals(memberId, date);

        Map<String, Object> metrics = buildDailyMetrics(actual, target, meals);
        AiDailyReportResult aiResult = actual.isEmpty()
                ? buildEmptyDailyAiResult()
                : requestDailyAiReport(buildDailyPrompt(member, date, actual, target, metrics, meals));

        DailyReport report = DailyReport.builder()
                .memberId(memberId)
                .reportDate(date)
                .aiScore(aiResult.getScore())
                .grade(aiResult.getGrade())
                .summary(aiResult.getSummary())
                .highlights(toJson(aiResult.getHighlights()))
                .improvements(toJson(aiResult.getImprovements()))
                .recommendations(toJson(aiResult.getRecommendations()))
                .nutrientScores(toJson(aiResult.getNutrientScores()))
                .riskFlags(toJson(aiResult.getRiskFlags()))
                .metrics(toJson(metrics))
                .reportVersion(REPORT_VERSION)
                .build();

        dailyReportRepository.upsert(report);
        return report;
    }

    @Transactional(readOnly = true)
    public Optional<DailyReport> getDailyReport(Long memberId, LocalDate date) {
        return dailyReportRepository.findByMemberAndDate(memberId, date);
    }

    @Transactional
    public WeeklyReport generateWeeklyReport(Long memberId, LocalDate startDate, LocalDate endDate) {
        Member member = getMember(memberId);
        List<LocalDate> dates = buildDateRange(startDate, endDate);
        List<Map<String, Object>> dailyMetrics = new ArrayList<>();

        for (LocalDate date : dates) {
            MemberNutrientDaily target = memberNutrientService.getDaily(memberId, date);
            MealLogDailyTotal dailyTotal = mealLogRepository.findDailyTotal(memberId, date);
            NutritionSnapshot actual = NutritionSnapshot.from(dailyTotal);
            List<MealWithFoodsResponse> meals = mealService.getMeals(memberId, date);
            Map<String, Object> metrics = buildDailyMetrics(actual, target, meals);
            metrics.put("date", date.toString());
            dailyMetrics.add(metrics);
        }

        AiWeeklyReportResult aiResult = requestWeeklyAiReport(buildWeeklyPrompt(member, startDate, endDate, dailyMetrics));
        LocalDate bestDay = normalizeDate(parseDate(aiResult.getBestDay()), startDate, endDate);
        LocalDate worstDay = normalizeDate(parseDate(aiResult.getWorstDay()), startDate, endDate);

        WeeklyReport report = WeeklyReport.builder()
                .memberId(memberId)
                .aiScore(aiResult.getScore())
                .grade(aiResult.getGrade())
                .startDate(startDate)
                .endDate(endDate)
                .summary(aiResult.getSummary())
                .patternSummary(aiResult.getPatternSummary())
                .bestDay(bestDay)
                .bestReason(aiResult.getBestReason())
                .worstDay(worstDay)
                .worstReason(aiResult.getWorstReason())
                .nextWeekFocus(aiResult.getNextWeekFocus())
                .highlights(toJson(aiResult.getHighlights()))
                .improvements(toJson(aiResult.getImprovements()))
                .recommendations(toJson(aiResult.getRecommendations()))
                .trend(toJson(buildTrendMap(aiResult, bestDay, worstDay)))
                .riskFlags(toJson(aiResult.getRiskFlags()))
                .consistencyScore(aiResult.getConsistencyScore())
                .reportVersion(REPORT_VERSION)
                .build();

        weeklyReportRepository.upsert(report);
        return report;
    }

    @Transactional(readOnly = true)
    public Optional<WeeklyReport> getWeeklyReport(Long memberId, LocalDate date) {
        LocalDate startDate = date.with(DayOfWeek.MONDAY);
        LocalDate endDate = startDate.plusDays(6);
        return weeklyReportRepository.findByMemberAndRange(memberId, startDate, endDate);
    }

    public DailyReportResponse toDailyResponse(DailyReport report) {
        return DailyReportResponse.builder()
                .reportDate(report.getReportDate())
                .score(report.getAiScore())
                .grade(report.getGrade())
                .summary(report.getSummary())
                .highlights(parseJson(report.getHighlights(), new TypeReference<List<String>>() {}, List.of()))
                .improvements(parseJson(report.getImprovements(), new TypeReference<List<String>>() {}, List.of()))
                .recommendations(parseJson(report.getRecommendations(), new TypeReference<List<String>>() {}, List.of()))
                .nutrientScores(parseJson(report.getNutrientScores(), new TypeReference<Map<String, Integer>>() {}, Map.of()))
                .riskFlags(parseJson(report.getRiskFlags(), new TypeReference<List<String>>() {}, List.of()))
                .metrics(parseJson(report.getMetrics(), new TypeReference<Map<String, Object>>() {}, Map.of()))
                .updatedAt(report.getUpdatedAt())
                .build();
    }

    public WeeklyReportResponse toWeeklyResponse(WeeklyReport report) {
        List<DailyScorePoint> scores = dailyReportRepository.findScoresByMemberAndRange(
                report.getMemberId(), report.getStartDate(), report.getEndDate());
        List<WeeklyDailyScoreResponse> dailyScores = scores.stream()
                .map(score -> WeeklyDailyScoreResponse.builder()
                        .date(score.getDate())
                        .score(score.getScore())
                        .build())
                .toList();
        Map<String, Object> trendMap = parseJson(report.getTrend(), new TypeReference<Map<String, Object>>() {}, null);
        if (trendMap == null || trendMap.isEmpty()) {
            trendMap = buildTrendMap(report);
        }
        return WeeklyReportResponse.builder()
                .startDate(report.getStartDate())
                .endDate(report.getEndDate())
                .score(report.getAiScore())
                .grade(report.getGrade())
                .summary(report.getSummary())
                .patternSummary(report.getPatternSummary())
                .bestDay(report.getBestDay())
                .bestReason(report.getBestReason())
                .worstDay(report.getWorstDay())
                .worstReason(report.getWorstReason())
                .nextWeekFocus(report.getNextWeekFocus())
                .highlights(parseJson(report.getHighlights(), new TypeReference<List<String>>() {}, List.of()))
                .improvements(parseJson(report.getImprovements(), new TypeReference<List<String>>() {}, List.of()))
                .recommendations(parseJson(report.getRecommendations(), new TypeReference<List<String>>() {}, List.of()))
                .trend(trendMap == null ? Map.of() : trendMap)
                .riskFlags(parseJson(report.getRiskFlags(), new TypeReference<List<String>>() {}, List.of()))
                .consistencyScore(report.getConsistencyScore())
                .dailyScores(dailyScores)
                .updatedAt(report.getUpdatedAt())
                .build();
    }

    public LocalDate weekStart(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    public LocalDate weekEnd(LocalDate date) {
        return weekStart(date).plusDays(6);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
    }

    private AiDailyReportResult buildEmptyDailyAiResult() {
        AiDailyReportResult result = new AiDailyReportResult();
        result.setScore(0);
        result.setGrade("집중 개선 필요");
        result.setSummary("해당 날짜에 기록된 식단이 없습니다. 한 끼라도 기록하면 분석 정확도가 올라갑니다.");
        result.setHighlights(List.of("식단 기록을 시작해 보세요."));
        result.setImprovements(List.of("오늘 먹은 메뉴를 한 끼라도 입력해 보세요."));
        result.setRecommendations(List.of("내일 아침에 먹은 음식 1개를 먼저 기록하기"));
        result.setRiskFlags(List.of("기록 부족"));
        result.setNutrientScores(Map.of());
        return result;
    }

    private Map<String, Object> buildDailyPrompt(Member member,
                                                 LocalDate date,
                                                 NutritionSnapshot actual,
                                                 MemberNutrientDaily target,
                                                 Map<String, Object> metrics,
                                                 List<MealWithFoodsResponse> meals) {
        Map<String, Object> prompt = new HashMap<>();
        prompt.put("type", "daily");
        prompt.put("date", date.toString());
        prompt.put("member", Map.of(
                "gender", member.getGender(),
                "heightCm", member.getHeightCm(),
                "weightKg", member.getWeightKg()
        ));
        prompt.put("actual", toMap(actual));
        prompt.put("target", toMap(NutritionSnapshot.from(target)));
        prompt.put("metrics", metrics);
        prompt.put("mealSummary", buildMealSummary(meals));
        return prompt;
    }

    private Map<String, Object> buildWeeklyPrompt(Member member,
                                                  LocalDate startDate,
                                                  LocalDate endDate,
                                                  List<Map<String, Object>> dailyMetrics) {
        Map<String, Object> prompt = new HashMap<>();
        prompt.put("type", "weekly");
        prompt.put("period", Map.of(
                "startDate", startDate.toString(),
                "endDate", endDate.toString()
        ));
        prompt.put("member", Map.of(
                "gender", member.getGender(),
                "heightCm", member.getHeightCm(),
                "weightKg", member.getWeightKg()
        ));
        prompt.put("dailyMetrics", dailyMetrics);
        return prompt;
    }

    private Map<String, Object> buildMealSummary(List<MealWithFoodsResponse> meals) {
        Map<String, Object> summary = new HashMap<>();
        if (meals == null) {
            summary.put("meals", List.of());
            return summary;
        }
        List<Map<String, Object>> mealList = new ArrayList<>();
        for (MealWithFoodsResponse meal : meals) {
            Map<String, Object> item = new HashMap<>();
            item.put("mealType", meal.getMealType());
            item.put("kcal", meal.getNutrition() != null ? meal.getNutrition().getKcal() : BigDecimal.ZERO);
            item.put("foods", meal.getFoods() == null ? List.of() : meal.getFoods().stream()
                    .map(food -> Map.of(
                            "name", food.getName(),
                            "intake", food.getIntake(),
                            "kcal", food.getKcal()
                    ))
                    .toList());
            mealList.add(item);
        }
        summary.put("meals", mealList);
        return summary;
    }

    private List<LocalDate> buildDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate cursor = startDate;
        while (!cursor.isAfter(endDate)) {
            dates.add(cursor);
            cursor = cursor.plusDays(1);
        }
        return dates;
    }

    private Map<String, Object> buildDailyMetrics(NutritionSnapshot actual,
                                                  MemberNutrientDaily target,
                                                  List<MealWithFoodsResponse> meals) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("actual", toMap(actual));
        metrics.put("target", toMap(NutritionSnapshot.from(target)));
        metrics.put("mealPattern", buildMealPatternMetrics(meals));
        return metrics;
    }

    private Map<String, Object> buildMealPatternMetrics(List<MealWithFoodsResponse> meals) {
        int mealCount = 0;
        BigDecimal totalKcal = BigDecimal.ZERO;
        BigDecimal snackKcal = BigDecimal.ZERO;
        BigDecimal dinnerKcal = BigDecimal.ZERO;
        int foodVariety = 0;
        if (meals != null) {
            Map<Long, Boolean> foodMap = new HashMap<>();
            for (MealWithFoodsResponse meal : meals) {
                if (meal.getFoods() != null && !meal.getFoods().isEmpty()) {
                    mealCount += 1;
                    meal.getFoods().forEach(food -> {
                        if (food.getFoodId() != null) {
                            foodMap.put(food.getFoodId(), true);
                        }
                    });
                }
                BigDecimal kcal = meal.getNutrition() != null && meal.getNutrition().getKcal() != null
                        ? meal.getNutrition().getKcal()
                        : BigDecimal.ZERO;
                totalKcal = totalKcal.add(kcal);
                if (meal.getMealType() == com.ssafy.bablog.meal.domain.MealType.SNACK) {
                    snackKcal = snackKcal.add(kcal);
                }
                if (meal.getMealType() == com.ssafy.bablog.meal.domain.MealType.DINNER) {
                    dinnerKcal = dinnerKcal.add(kcal);
                }
            }
            foodVariety = foodMap.size();
        }
        BigDecimal snackRatio = ratio(snackKcal, totalKcal);
        BigDecimal dinnerRatio = ratio(dinnerKcal, totalKcal);
        Map<String, Object> result = new HashMap<>();
        result.put("mealCount", mealCount);
        result.put("snackRatio", snackRatio);
        result.put("dinnerRatio", dinnerRatio);
        result.put("foodVariety", foodVariety);
        return result;
    }

    private AiDailyReportResult requestDailyAiReport(Map<String, Object> payload) {
        try {
            return reportAiService.generateDailyInsight(payload, AiDailyReportResult.class);
        } catch (Exception e) {
            return buildEmptyDailyAiResult();
        }
    }

    private AiWeeklyReportResult requestWeeklyAiReport(Map<String, Object> payload) {
        try {
            return reportAiService.generateWeeklyInsight(payload, AiWeeklyReportResult.class);
        } catch (Exception e) {
            AiWeeklyReportResult fallback = new AiWeeklyReportResult();
            fallback.setScore(0);
            fallback.setGrade("집중 개선 필요");
            fallback.setConsistencyScore(0);
            fallback.setSummary("주간 식단 데이터를 충분히 수집하지 못했습니다.");
            fallback.setPatternSummary("주간 패턴을 분석할 데이터가 부족합니다.");
            fallback.setBestDay(null);
            fallback.setBestReason("기록이 부족해 최고일을 선정할 수 없습니다.");
            fallback.setWorstDay(null);
            fallback.setWorstReason("기록이 부족해 최저일을 선정할 수 없습니다.");
            fallback.setNextWeekFocus("다음 주에는 최소 3일 이상 기록해 보세요.");
            fallback.setHighlights(List.of("기록을 꾸준히 남긴 점이 좋습니다."));
            fallback.setImprovements(List.of("일주일 동안 최소 3일 이상 기록해 보세요."));
            fallback.setRecommendations(List.of("다음 주에는 아침 식사부터 기록하기"));
            fallback.setRiskFlags(List.of("기록 부족"));
            fallback.setTrend(Map.of());
            return fallback;
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            if (value instanceof Map) {
                return "{}";
            }
            return "[]";
        }
    }

    public <T> T parseJson(String raw, TypeReference<T> type, T fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            T value = objectMapper.readValue(raw, type);
            return value == null ? fallback : value;
        } catch (Exception e) {
            return fallback;
        }
    }

    private BigDecimal ratio(BigDecimal part, BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return part.divide(total, 4, RoundingMode.HALF_UP);
    }

    private Map<String, Object> toMap(NutritionSnapshot snapshot) {
        Map<String, Object> map = new HashMap<>();
        map.put("kcal", snapshot.getKcal());
        map.put("protein", snapshot.getProtein());
        map.put("fat", snapshot.getFat());
        map.put("saturatedFat", snapshot.getSaturatedFat());
        map.put("transFat", snapshot.getTransFat());
        map.put("carbohydrates", snapshot.getCarbohydrates());
        map.put("sugar", snapshot.getSugar());
        map.put("natrium", snapshot.getNatrium());
        map.put("cholesterol", snapshot.getCholesterol());
        return map;
    }

    private LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(raw);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> buildTrendMap(AiWeeklyReportResult result, LocalDate bestDay, LocalDate worstDay) {
        Map<String, Object> trend = new HashMap<>();
        trend.put("patternSummary", result.getPatternSummary());
        trend.put("bestDay", bestDay != null ? bestDay.toString() : null);
        trend.put("worstDay", worstDay != null ? worstDay.toString() : null);
        trend.put("nextWeekFocus", result.getNextWeekFocus());
        return trend;
    }

    private Map<String, Object> buildTrendMap(WeeklyReport report) {
        Map<String, Object> trend = new HashMap<>();
        trend.put("patternSummary", report.getPatternSummary());
        trend.put("bestDay", report.getBestDay() != null ? report.getBestDay().toString() : null);
        trend.put("worstDay", report.getWorstDay() != null ? report.getWorstDay().toString() : null);
        trend.put("nextWeekFocus", report.getNextWeekFocus());
        return trend;
    }

    private LocalDate normalizeDate(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null) {
            return null;
        }
        if (date.isBefore(startDate) || date.isAfter(endDate)) {
            return null;
        }
        return date;
    }
}
