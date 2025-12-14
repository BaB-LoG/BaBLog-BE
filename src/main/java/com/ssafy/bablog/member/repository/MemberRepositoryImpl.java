package com.ssafy.bablog.member.repository;

import com.ssafy.bablog.member.domain.Member;
import com.ssafy.bablog.member.repository.mapper.MemberMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberMapper memberMapper;

    public MemberRepositoryImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    @Transactional
    public Member save(Member member) {
        memberMapper.insert(member);
        return member;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(memberMapper.findByEmail(email));
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(memberMapper.findById(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberMapper.countByEmail(email) > 0;
    }

    @Override
    @Transactional
    public void update(Member member) {
        memberMapper.update(member);
    }

    @Override
    @Transactional
    public void updatePassword(Long memberId, String encodedPassword) {
        memberMapper.updatePassword(memberId, encodedPassword);
    }

    @Override
    @Transactional
    public void deleteById(Long memberId) {
        memberMapper.deleteById(memberId);
    }

    @Override
    public java.util.List<Long> findAllIds() {
        return memberMapper.findAllIds();
    }
}
