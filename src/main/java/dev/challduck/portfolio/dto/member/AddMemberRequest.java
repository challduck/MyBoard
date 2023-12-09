package dev.challduck.portfolio.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
    private String email;
    private String nickname;
    private String password;
}
