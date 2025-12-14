package com.ssafy.bablog.meal.service;

import com.ssafy.bablog.food.domain.Food;
import com.ssafy.bablog.food.repository.FoodRepository;
import com.ssafy.bablog.meal.controller.dto.AddMealFoodRequest;
import com.ssafy.bablog.meal.controller.dto.AddMealFoodResponse;
import com.ssafy.bablog.meal.controller.dto.MealFoodResponse;
import com.ssafy.bablog.meal.controller.dto.MealResponse;
import com.ssafy.bablog.meal.controller.dto.MealWithFoodsResponse;
import com.ssafy.bablog.meal.controller.dto.UpdateMealFoodRequest;
import com.ssafy.bablog.meal.domain.Meal;
import com.ssafy.bablog.meal.domain.MealFood;
import com.ssafy.bablog.meal.domain.MealType;
import com.ssafy.bablog.meal.repository.MealFoodRepository;
import com.ssafy.bablog.meal.repository.MealRepository;
import com.ssafy.bablog.meal.repository.mapper.MealFoodWithFood;
import com.ssafy.bablog.meal_log.domain.MealLog;
import com.ssafy.bablog.meal_log.repository.MealLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final FoodRepository foodRepository;
    private final MealLogRepository mealLogRepository;

    /**
     * Member 1명의 하루치 기본 Meal 레코드를 생성 (존재하면 건너뜀)
     */
    public void createDailyMeals(Long memberId, LocalDate mealDate) {
        for (MealType mealType : MealType.values()) {
            mealRepository.findByMemberAndTypeAndDate(memberId, mealType, mealDate)
                    .orElseGet(() -> mealRepository.save(
                            Meal.builder()
                                    .memberId(memberId)
                                    .mealType(mealType)
                                    .mealDate(mealDate)
                                    .build()));
        }
    }

    /**
     * 식단 추가
     * 식단에 Food 추가 및 영양소 누적
     */
    public AddMealFoodResponse addFoodToMeal(Long memberId, AddMealFoodRequest request) {
        // 1. 어느 사용자의 아침 / 점심 / 저녁 / 간식 중 어떤 형태의 며칠날 식단인지 찾기
        Meal meal = mealRepository.findByMemberAndTypeAndDate(memberId, request.getMealType(), request.getMealDate())
                .orElseGet(() -> mealRepository.save(Meal.builder()
                        .memberId(memberId)
                        .mealType(request.getMealType())
                        .mealDate(request.getMealDate())
                        .build()));
        // 2. 요청을 보낸 사용자가 이 식단의 소유주가 맞는지 검증
        ensureMealOwner(meal, memberId);

        // 3. foodId를 통해 food를 찾기
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "음식 정보를 찾을 수 없습니다."));

        // 4. meal과 food의 중간 테이블
        MealFood mealFood = MealFood.builder()
                .mealId(meal.getId())
                .foodId(food.getId())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .build();
        // 5. 저장하기
        mealFoodRepository.save(mealFood);

        // 6. 추가한 음식의 영양 정보를 해당 날짜의 mealLog에 누적하기
        MealLog mealLogDelta = MealLog.from(meal, food, request.getQuantity());

        // 7. 만약 meal_log가 없다면 생성, 그리고 누적하기
        mealLogRepository.upsertNutrition(mealLogDelta);

        // 8. meal 테이블에도 영양 정보를 누적
        Meal mealDelta = Meal.nutritionDelta(food, request.getQuantity());
        adjustNutrition(meal, mealDelta);

        return AddMealFoodResponse.of(
                MealResponse.from(meal),
                MealFoodResponse.from(mealFood, food)
        );
    }

    /**
     * 날짜별 식단 조회
     * member의 mealDate 일자의 BREAK_FAST, LUNCH, DINNER, SNACK을 모두 조회해 오는 메서드
     */
    @Transactional(readOnly = true)
    public List<MealWithFoodsResponse> getMeals(Long memberId, LocalDate mealDate) {
        List<Meal> meals = mealRepository.findByMemberAndDate(memberId, mealDate);
        List<Long> mealIds = meals.stream().map(Meal::getId).toList();
        Map<Long, List<MealFoodResponse>> foodsByMeal = buildMealFoodResponses(mealIds);

        return meals.stream()
                .map(meal -> MealWithFoodsResponse.withNutrition(meal, foodsByMeal.getOrDefault(meal.getId(), List.of())))
                .toList();
    }

    /**
     * 식단 단일 조회
     * BREAK_FAST 단일 조회, LUNCH 단일 조회...
     * 클라이언트에 mealId가 있고, mealId가 있으면 날짜, 아침 점심 저녁 간식을 구분하지 않아도 되기 때문에
     * mealId를 받아오는 형태로 구현
     */
    @Transactional(readOnly = true)
    public MealWithFoodsResponse getMeal(Long memberId, Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "식단 정보를 찾을 수 없습니다."));
        ensureMealOwner(meal, memberId);
        Map<Long, List<MealFoodResponse>> foodsByMeal = buildMealFoodResponses(List.of(mealId));
        return MealWithFoodsResponse.withoutNutrition(meal, foodsByMeal.getOrDefault(meal.getId(), List.of()));
    }

    /**
     * mealFood 삭제
     * 삭제 시 해당 일자에 누적된 meal, meal_log의 영양 정보에도 반영(adjustNutrition 메서드)
     */
    public void deleteMealFood(Long memberId, Long mealFoodId) {
        MealFoodContext context = loadMealFoodContext(mealFoodId, memberId);

        adjustNutrition(context.meal(), Meal.reverseDelta(Meal.nutritionDelta(context.food(), context.mealFood().getQuantity())));
        mealLogRepository.upsertNutrition(MealLog.from(context.meal(), context.food(), context.mealFood().getQuantity()).reverseDelta());

        mealFoodRepository.deleteById(mealFoodId);
    }

    /**
     * mealfood 수정
     * 식단 음식 수정 시 기존값만큼 차감 이후 새값만큼 증가
     */
    public MealWithFoodsResponse updateMealFood(Long memberId, Long mealFoodId, UpdateMealFoodRequest request) {
        MealFoodContext context = loadMealFoodContext(mealFoodId, memberId);

        if (!context.mealFood().getMealId().equals(request.getMealId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mealId가 기존 식단와 일치하지 않습니다.");
        }

        // 기존 값
        // 요청값이 없으면 기존 값 유지
        MealFood existing = context.mealFood();
        Food oldFood = context.food();
        BigDecimal oldQuantity = existing.getQuantity();

        Food newFood = request.getFoodId() != null
                ? foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "음식 정보를 찾을 수 없습니다."))
                : oldFood;
        BigDecimal newQuantity = request.getQuantity() != null ? request.getQuantity() : oldQuantity;
        String newUnit = request.getUnit() != null ? request.getUnit() : existing.getUnit();

        // 기존 영양 차감
        adjustNutrition(context.meal(), Meal.reverseDelta(Meal.nutritionDelta(oldFood, oldQuantity)));
        mealLogRepository.upsertNutrition(MealLog.from(context.meal(), oldFood, oldQuantity).reverseDelta());

        // 신규 값 적용
        existing.update(newFood.getId(), newQuantity, newUnit);
        mealFoodRepository.update(existing);
        adjustNutrition(context.meal(), Meal.nutritionDelta(newFood, newQuantity));
        mealLogRepository.upsertNutrition(MealLog.from(context.meal(), newFood, newQuantity));

        Map<Long, List<MealFoodResponse>> foodsByMeal = buildMealFoodResponses(List.of(context.meal().getId()));
        return MealWithFoodsResponse.withNutrition(context.meal(), foodsByMeal.getOrDefault(context.meal().getId(), List.of()));
    }

    // -------------------------------------  이하 private  ---------------------------------------

    private void ensureMealOwner(Meal meal, Long memberId) {
        if (!meal.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 식단만 수정할 수 있습니다.");
        }
    }

    private void adjustNutrition(Meal meal, Meal delta) {
        mealRepository.adjustNutrition(meal.getId(), delta);
        meal.applyNutritionDelta(delta);
    }

    private MealFoodContext loadMealFoodContext(Long mealFoodId, Long memberId) {
        MealFood mealFood = mealFoodRepository.findById(mealFoodId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "식단에 추가된 음식을 찾을 수 없습니다."));

        Meal meal = mealRepository.findById(mealFood.getMealId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "식단 정보를 찾을 수 없습니다."));
        ensureMealOwner(meal, memberId);

        Food food = foodRepository.findById(mealFood.getFoodId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "음식 정보를 찾을 수 없습니다."));

        return new MealFoodContext(meal, mealFood, food);
    }

    private Map<Long, List<MealFoodResponse>> buildMealFoodResponses(List<Long> mealIds) {
        if (mealIds.isEmpty()) {
            return Map.of();
        }

        List<MealFoodWithFood> rows = mealFoodRepository.findByMealIdsWithFood(mealIds);

        Map<Long, List<MealFoodResponse>> map = new HashMap<>();
        for (MealFoodWithFood row : rows) {
            MealFoodResponse from = MealFoodResponse.from(row.getMealFood(), row.getFood());
            map.computeIfAbsent(row.getMealFood().getMealId(), k -> new ArrayList<>()).add(from);
        }
        return map;
    }

    /**
     * meal_food 수정/삭제 시 사용하는 조회 컨텍스트
     */
    private record MealFoodContext(Meal meal, MealFood mealFood, Food food) {
    }
}
