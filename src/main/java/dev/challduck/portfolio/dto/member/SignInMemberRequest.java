package dev.challduck.portfolio.dto.member;

import dev.challduck.portfolio.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInMemberRequest {
    @Schema(description = "이메일", example = "newmember@naver.com")
    private String email;
    @Schema(description = "비밀번호", example = "password1234")
    private String password;

//    public Member toEntity(){
//        return Member.builder()
//                .email(email)
//                .password(password)
//            .build();
//    }
}
