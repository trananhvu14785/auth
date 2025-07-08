package com.kane.auth.controller;

import com.kane.auth.Mapper.UserAccountMapper;
import com.kane.auth.Service.UserAccountService;
import com.kane.auth.model.UserAccount;
import com.kane.common.dto.request.SignInRequest;
import com.kane.common.dto.request.SignUpRequest;
import com.kane.common.dto.response.SignInResponse;
import com.kane.common.util.JwtUtils;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserAccountService userAccountService;
  private final JwtUtils jwtUtils;

  //    public AuthController(UserAccountService userAccountService) {
  //        this.userAccountService = userAccountService;
  //    }
  //    @PostMapping("/auth")
  //    public ResponseEntity<String> auth() {
  //        return ResponseEntity.ok("This is auth service");
  //    }

  @PostMapping("/signUp")
  public ResponseEntity<Boolean> signUp(@RequestBody SignUpRequest signUpRequest) {
    UserAccount userAccount = UserAccountMapper.INSTANCE.toUserAccount(signUpRequest);
    return ResponseEntity.ok(this.userAccountService.save(userAccount));
  }

  @PostMapping("/signIn")
  public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signIpRequest) {
    try {
      String accessToken = this.jwtUtils.generateToken(signIpRequest.getUsername());
      Date expiredDate = this.jwtUtils.extractExpiration(accessToken);
      String refreshToken = this.jwtUtils.generateRefreshToken(signIpRequest.getUsername());
      return ResponseEntity.ok(
          SignInResponse.builder()
              .token(accessToken)
              .refreshToken(refreshToken)
              .expiredDate(expiredDate)
              .build());
    } catch (Exception e) {
      throw new RuntimeException("Invalid username or password", e);
    }
  }

  @GetMapping("/hello")
  public String hello() {
    return "Hello from Auth Service";
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
    String refreshToken = request.get("refreshToken");
    if (!StringUtils.hasText(refreshToken) || jwtUtils.isTokenExpired(refreshToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Refresh token is invalid or expired");
    }

    String username = jwtUtils.extractUsername(refreshToken);
    String newAccessToken = jwtUtils.generateToken(username);

    return ResponseEntity.ok(Map.of("token", newAccessToken));
  }
}
