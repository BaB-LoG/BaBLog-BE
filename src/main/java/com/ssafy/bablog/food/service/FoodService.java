package com.ssafy.bablog.food.service;

import com.ssafy.bablog.food.domain.Food;
import com.ssafy.bablog.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodService {

    private final FoodRepository foodRepository;

    public List<Food> search(String name, String vendor) {
        String normalizedName = normalize(name);
        String normalizedVendor = normalize(vendor);
        if (normalizedName == null && normalizedVendor == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name 또는 vendor 중 최소 1개를 입력해야 합니다.");
        }
        return foodRepository.search(normalizedName, normalizedVendor);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed.toLowerCase();
    }
}
