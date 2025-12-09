package com.ssafy.bablog.member.controller;

import com.ssafy.bablog.member.controller.dto.*;
import com.ssafy.bablog.security.MemberPrincipal;
import com.ssafy.bablog.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        memberService.signup(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        memberService.logout(authorizationHeader);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<MemberResponse> getMemberInfo(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberResponse response = memberService.getMemberInfo(principal.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 수정
     */
    @PatchMapping("/info")
    public ResponseEntity<MemberResponse> updateMember(@AuthenticationPrincipal MemberPrincipal principal,
                                                       @Valid @RequestBody UpdateMemberRequest request) {
        MemberResponse response = memberService.updateMember(principal.getId(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/sign-out")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal MemberPrincipal principal,
                                             @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                             @Valid @RequestBody PasswordCheckRequest request) {
        memberService.deleteMember(principal.getId(), authorizationHeader, request.getPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * 비밀번호 확인
     */
    @PostMapping("/password-check")
    public ResponseEntity<Boolean> checkPassword(@AuthenticationPrincipal MemberPrincipal principal,
                                                 @Valid @RequestBody PasswordCheckRequest request) {
        boolean matches = memberService.checkPassword(principal.getId(), request.getPassword());
        return ResponseEntity.ok(matches);
    }

    /**
     * 비밀번호 수정
     */
    @PostMapping("/password-change")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal MemberPrincipal principal,
                                               @Valid @RequestBody ChangePasswordRequest request) {
        memberService.changePassword(principal.getId(), request);
        return ResponseEntity.noContent().build();
    }
}
