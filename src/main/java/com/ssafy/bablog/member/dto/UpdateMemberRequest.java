package com.ssafy.bablog.member.dto;

import com.ssafy.bablog.member.domain.Gender;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequest {
    @Size(min = 1, max = 50)
    private String name;

    private Gender gender;
    private LocalDate birthDate;
    private BigDecimal heightCm;
    private BigDecimal weightKg;
}
