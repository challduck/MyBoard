package dev.challduck.portfolio.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MEMBER("ROLE_MEMBER", "일반사용자"),
    MANAGER("ROLE_MANAGER", "일반매니저"),
    ADMIN("ROLE_ADMIN", "일반관리자");

    private final String key;
    private final String title;
}
