package com.ssafy.bablog.member.service;


import com.ssafy.bablog.member.controller.dto.*;
import com.ssafy.bablog.member.domain.Member;
import com.ssafy.bablog.member.repository.MemberRepository;

import com.ssafy.bablog.security.jwt.JwtTokenProvider;
import com.ssafy.bablog.security.MemberPrincipal;
import com.ssafy.bablog.security.jwt.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * 회원 가입
     */
    public void signup(SignupRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (memberRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.");
        }

        Member member = Member.builder()
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .build();

        memberRepository.save(member);
    }

    /**
     * 로그인
     */
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword()));

        MemberPrincipal principal = (MemberPrincipal) authentication.getPrincipal();
        Member member = memberRepository.findById(principal.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."));

        String token = jwtTokenProvider.generateToken(principal);
        return AuthResponse.of(token, MemberResponse.from(member));
    }

    /**
     * 로그 아웃
     */
    public void logout(String authorizationHeader) {
        String token = jwtTokenProvider.extractToken(authorizationHeader);
        jwtTokenProvider.checkIsUsableTokenString(token);
        var expiry = jwtTokenProvider.getExpiration(token);
        tokenBlacklistService.blacklist(token, expiry);
    }

    /**
     * 회원 정보 조회 (마이 페이지)
     */
    public MemberResponse getMemberInfo(Long memberId) {
        Member member = getMember(memberId);
        return MemberResponse.from(member);
    }

    /**
     * 회원 정보 수정
     */
    public MemberResponse updateMember(Long memberId, UpdateMemberRequest request) {
        Member member = getMember(memberId);
        member.update(request);
        memberRepository.update(member);
        return MemberResponse.from(member);
    }

    /**
     * 회원 탈퇴
     */
    public void deleteMember(Long memberId, String authorizationHeader, String password) {
        Member member = getMember(memberId);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }
        memberRepository.deleteById(memberId);

        // 현재 토큰 블랙리스트 처리
        String token = jwtTokenProvider.extractToken(authorizationHeader);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            tokenBlacklistService.blacklist(token, jwtTokenProvider.getExpiration(token));
        }
    }

    /**
     * 비밀번호 확인
     */
    public boolean checkPassword(Long memberId, String rawPassword) {
        Member member = getMember(memberId);
        return passwordEncoder.matches(rawPassword, member.getPassword());
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword(Long memberId, ChangePasswordRequest request) {
        Member member = getMember(memberId);
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }
        String encoded = passwordEncoder.encode(request.getNewPassword());
        memberRepository.updatePassword(memberId, encoded);
    }

    // ------------------------------  이하 private  ---------------------------------------------

    /**
     * 예외 처리를 포함하는 Member 조회 기능
     */
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
    }
}
