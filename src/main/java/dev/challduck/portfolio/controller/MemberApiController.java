package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.member.*;
import dev.challduck.portfolio.exception.IncorrectPasswordException;
import dev.challduck.portfolio.exception.InvalidPasswordException;
import dev.challduck.portfolio.exception.UserNotFoundException;
import dev.challduck.portfolio.service.MemberService;
import dev.challduck.portfolio.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "Member Api docs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class MemberApiController {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "201",description = "회원가입을 성공하였습니다.")
    @ApiResponse(responseCode = "400",description = "잘못된 요청 형식입니다.")
    @ApiResponse(responseCode = "409",description = "이미 존재하는 이메일 입니다.")
    // 사용자 회원가입 요청
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AddMemberRequest request){
        try {
            memberService.saveMember(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AddMemberResponse(HttpStatus.CREATED.value(), "회원가입을 성공했습니다."));
        }
        catch (DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "로그인", description = "로그인을 시도합니다.")
    @ApiResponse(responseCode = "200",description = "로그인을 성공하였습니다.")
    @ApiResponse(responseCode = "400",description = "아이디 또는 비밀번호가 일치하지않습니다.")
    @PostMapping("/login")
    public ResponseEntity<String> signIn(@RequestBody SignInMemberRequest dto, HttpServletResponse response, HttpServletRequest request){

        try {
            Member member = memberService.signIn(dto);
            // token 발급하는 부분
            String refreshToken = refreshTokenService.loginSuccessMemberGenerateRefreshToken(member);
            String accessToken = refreshTokenService.loginSuccessMemberGenerateAccessToken(member);

            // Refresh Token을 쿠키에 저장
            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 유효 시간을 초 단위로 설정 (7일로 설정 예시)
            refreshCookie.setPath("/"); // 쿠키의 경로 설정
            response.addCookie(refreshCookie);

            // Access Token을 헤더에 추가 및 본문에 담아 반환
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body("로그인을 성공하였습니다." + refreshToken);
        }
        catch (UserNotFoundException | IncorrectPasswordException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "로그아웃",description = "로그아웃을 시도합니다.")
    @ApiResponse(responseCode = "200",description = "로그아웃을 성공하였습니다.")
    @ApiResponse(responseCode = "400",description = "잘못된 요청입니다.")
    @ApiResponse(responseCode = "401",description = "등록된 회원이 아닙니다.")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(){
        try {
            memberService.logout();
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "회원 정보 조회", description = "사용자의 회원정보를 조회합니다.")
    @ApiResponse(responseCode = "200",description = "회원정보 조회를 성공하였습니다.",
            headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "가져올 회원정보가 존재하지않습니다.")
    @ApiResponse(responseCode = "401",description = "로그인한 사용자만 회원정보를 조회할 수 있습니다.")
    @GetMapping("/my-page")
    public ResponseEntity<MemberDataResponse> getMyPage(){
        try {
            MemberDataResponse myData = memberService.getMyData();
            return ResponseEntity.ok().body(myData);
        }
        catch (UsernameNotFoundException e){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch (IllegalArgumentException e){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return null;
    }

    @Operation(summary = "회원 닉네임 변경", description = "사용자의 닉네임을 변경합니다.")
    @ApiResponse(responseCode = "200",description = "닉네임 변경을 성공하였습니다.",
            headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "변경할 회원의 정보가 존재하지않습니다.")
    @ApiResponse(responseCode = "401",description = "로그인한 사용자만 닉네임을 변경 할 수 있습니다.")
    @PostMapping("/new-nickname")
    public ResponseEntity<Void> changeNickname(@RequestBody UpdateMemberNicknameRequest request){
        try {
            memberService.changeNickname(request);
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "회원 비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    @ApiResponse(responseCode = "200",description = "비밀번호 변경을 성공하였습니다.",
            headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "기존 비밀번호가 일치하지 않습니다.")
    @ApiResponse(responseCode = "401",description = "올바르지 않은 접근입니다.")
    @PostMapping("/new-password")
    public ResponseEntity<String> changePassword(@RequestBody UpdateMemberPasswordRequest request){
        try {
            memberService.changePassword(request);
            return ResponseEntity.ok().build();
        } catch (InvalidPasswordException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
