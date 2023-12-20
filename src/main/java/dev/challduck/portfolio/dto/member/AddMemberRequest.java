package dev.challduck.portfolio.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
    @Schema(description = "이메일", example = "newmember@naver.com")
    private String email;
    @Schema(description = "닉네임", example = "bbs_member")
    private String nickname;
    @Schema(description = "비밀번호", example = "password1234")
    private String password;
}
