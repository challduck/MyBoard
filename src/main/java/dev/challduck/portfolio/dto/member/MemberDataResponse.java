package dev.challduck.portfolio.dto.member;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.MemberLoginLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDataResponse {
    @Schema(description = "이메일", example = "newmember@naver.com")
    private final String email;
    @Schema(description = "닉네임", example = "bbs_member")
    private final String nickname;
    @Schema(description = "OAuth사용자", example = "false")
    private final boolean isOauth;
    private final LocalDateTime createdAt;
    private final String loginIp;
    private final LocalDateTime loginDate;

    public MemberDataResponse(Member member, boolean isOauth, MemberLoginLog memberLoginLog){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.createdAt = member.getCreatedAt();
        this.isOauth = isOauth;
        this.loginIp = memberLoginLog.getLoginIp();
        this.loginDate = memberLoginLog.getLoginDate();
    }
}
