package dev.challduck.portfolio.dto.member;

import dev.challduck.portfolio.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInMemberRequest {
    private String email;
    private String password;

//    public Member toEntity(){
//        return Member.builder()
//                .email(email)
//                .password(password)
//            .build();
//    }
}
