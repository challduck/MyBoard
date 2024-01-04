package dev.challduck.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_login_log")
public class MemberLoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_login_id", updatable = false)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "last_login_ip")
    private String loginIp;

    @Column(name = "last_login")
    private LocalDateTime loginDate;

    @Builder
    public MemberLoginLog(Member member,String loginIp ,LocalDateTime loginDate){
        this.member = member;
        this.loginIp = loginIp;
        this.loginDate = loginDate;
    }
}
