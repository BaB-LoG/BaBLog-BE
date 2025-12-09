package com.ssafy.bablog.member.repository;

import com.ssafy.bablog.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    boolean existsByEmail(String email);

    void update(Member member);

    void updatePassword(Long memberId, String encodedPassword);

    void deleteById(Long memberId);
}
