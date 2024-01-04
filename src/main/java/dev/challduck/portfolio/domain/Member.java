package dev.challduck.portfolio.domain;

import dev.challduck.portfolio.util.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Table(name = "member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Member(String email, String password, String nickname, Set<Role> roles){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<? extends GrantedAuthority> authorities = getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getKey()))
                .collect(Collectors.toSet());

        log.info("authorities : {}", authorities);
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 만료 되었는지 확인하는 로직
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금이 되어있는지 확인하는 로직
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // password가 만료되었는지 확인하는 로직
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true;
    }

    public Member updatePassword(String password) {
        this.password = password;
        return this;
    }

    public Member update(String name) {
        this.nickname = name;
        return this;
    }
}
