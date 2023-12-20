package dev.challduck.portfolio.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UpdateMemberNicknameRequest {
    @Schema(description = "변경할 닉네임", example = "newMember")
    private String newNickname;
}
