package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.AddMemberRequest;
import dev.challduck.portfolio.dto.AddMemberResponse;
import dev.challduck.portfolio.dto.SignInMemberRequest;
import dev.challduck.portfolio.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "Member 관련 Api docs 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class MemberApiController {
    private final MemberService memberService;

    @Operation(
            summary = "회원가입",
            description = "회원가입을 시도합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddMemberRequest.class))
    )

    // api 문서에 409 코드는 하나만 들어간다.. 1. vaild로 검증하는 로직을 구현해서 뭐가 문제인지 보내주기..?
    @ApiResponse(responseCode = "201",description = "회원가입을 성공하였습니다.")
    @ApiResponse(responseCode = "409",description = "이미 사용중인 닉네임입니다.")
    @ApiResponse(responseCode = "409",description = "이미 사용중인 이메일입니다.")
    @ApiResponse(responseCode = "400",description = "잘못된 요청 형식입니다.")
    // 사용자 회원가입 요청
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AddMemberRequest request, BindingResult result){
        if(memberService.checkNicknameDuplicate(request.getNickname())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AddMemberResponse(HttpStatus.CONFLICT.value(),"Nickname already exists"));
        }
        if (memberService.checkEmailDuplicate(request.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AddMemberResponse(HttpStatus.CONFLICT.value(),"Email already exists"));
        }
        if (result.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AddMemberResponse(HttpStatus.BAD_REQUEST.value(), "Validation error"));
        }
        memberService.saveMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AddMemberResponse(HttpStatus.CREATED.value(), "회원가입을 성공했습니다."));
    }

    @Operation(
            summary = "로그인",
            description = "로그인을 시도합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인을 성공하였습니다."
    )
    // 사용자 로그인 요청
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInMemberRequest request){
        if (memberService.signIn(request) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email or Password incorrect");
        }
        Member member = memberService.signIn(request);
        return ResponseEntity.status(HttpStatus.OK).body("로그인을 성공하였습니다.");
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 시도합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그아웃을 성공하였습니다."
    )
    // 사용자 로그아웃 요청
    @GetMapping("/logout")
    public String logout(){
        return "";
    }
}
