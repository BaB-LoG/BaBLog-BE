package com.ssafy.bablog.member.domain;

import com.ssafy.bablog.member.controller.dto.UpdateMemberRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long id;
    private String email;
    private String password;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(UpdateMemberRequest request) {
        this.name = Objects.isNull(request.getName()) ? this.name: request.getName();
        this.gender = Objects.isNull(request.getGender()) ? this.gender: request.getGender();
        this.birthDate = Objects.isNull(request.getBirthDate()) ? this.birthDate: request.getBirthDate();
        this.heightCm = Objects.isNull(request.getHeightCm()) ? this.heightCm: request.getHeightCm();
        this.weightKg = Objects.isNull(request.getWeightKg()) ? this.weightKg: request.getWeightKg();
    }
}
