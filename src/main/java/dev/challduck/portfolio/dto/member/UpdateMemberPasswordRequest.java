package dev.challduck.portfolio.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UpdateMemberPasswordRequest {
    @Schema(description = "기존 비밀번호", example = "password1234")
    private String existingPassword;
    @Schema(description = "새로운 비밀번호", example = "newPassword1234")
    private String newPassword;
}
