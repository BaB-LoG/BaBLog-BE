package com.ssafy.bablog.security;

import com.ssafy.bablog.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MemberPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final String name;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private MemberPrincipal(Long id, String email, String name, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.authorities = authorities;
    }

    public static MemberPrincipal from(Member member) {
        return new MemberPrincipal(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getName (){
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
