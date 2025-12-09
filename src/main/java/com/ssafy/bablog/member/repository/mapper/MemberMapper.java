package com.ssafy.bablog.member.repository.mapper;

import com.ssafy.bablog.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    Member findByEmail(@Param("email") String email);

    Member findById(@Param("id") Long id);

    int countByEmail(@Param("email") String email);

    void insert(Member member);

    void update(Member member);

    void updatePassword(@Param("id") Long id, @Param("password") String password);

    void deleteById(@Param("id") Long id);
}
