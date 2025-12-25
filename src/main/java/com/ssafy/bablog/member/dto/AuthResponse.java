package com.ssafy.bablog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String tokenType;
    private String accessToken;
    private MemberResponse member;

    public static AuthResponse of(String token, MemberResponse member) {
        return new AuthResponse("Bearer", token, member);
    }
}
