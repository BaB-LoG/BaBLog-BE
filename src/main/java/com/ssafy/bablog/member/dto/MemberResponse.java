package com.ssafy.bablog.member.dto;

import com.ssafy.bablog.member.domain.Gender;
import com.ssafy.bablog.member.domain.Member;
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
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private BigDecimal heightCm;
    private BigDecimal weightKg;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getHeightCm(),
                member.getWeightKg()
        );
    }
}
