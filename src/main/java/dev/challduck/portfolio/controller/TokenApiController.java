package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.dto.token.CreateAccessTokenRequest;
import dev.challduck.portfolio.dto.token.CreateAccessTokenResponse;
import dev.challduck.portfolio.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Token", description = "Token Api docs")
@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @Operation(summary = "AccessToken 재발급", description = "만료된 Access Token을 재발급 합니다.")
    @ApiResponse(responseCode = "200",description = "Token 재발급을 성공하였습니다.",
        headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request){

        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
