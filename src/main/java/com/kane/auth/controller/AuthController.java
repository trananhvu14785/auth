package com.kane.auth.controller;

import com.kane.auth.Mapper.UserAccountMapper;
import com.kane.auth.Service.AuthService;
import com.kane.auth.Service.UserAccountService;
import com.kane.auth.dto.response.UserAccountResponse;
import com.kane.auth.model.UserAccount;
import com.kane.common.dto.request.SignInRequest;
import com.kane.common.dto.request.SignUpRequest;
import com.kane.common.dto.response.SignInResponse;
import com.kane.common.exception.AccessDeniedCustomException;
import com.kane.common.util.JwtUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserAccountService userAccountService;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final AuthService authService;

  @PostMapping("/signUp")
  public ResponseEntity<Boolean> signUp(@RequestBody SignUpRequest signUpRequest) {
    UserAccount userAccount = UserAccountMapper.INSTANCE.toUserAccount(signUpRequest);
    return ResponseEntity.ok(this.userAccountService.save(userAccount));
  }

  @PostMapping("/signIn")
  public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signIpRequest) {
    return ResponseEntity.ok(authService.signIn(signIpRequest));
  }

  //  @PostMapping("/refreshToken")
  //  public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
  //    String refreshToken = request.get("refreshToken");
  //    if (!StringUtils.hasText(refreshToken) || jwtUtils.isTokenExpired(refreshToken)) {
  //      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
  //          .body("Refresh token is invalid or expired");
  //    }
  //
  //
  //    String username = jwtUtils.extractUsername(refreshToken);
  //    String newAccessToken = jwtUtils.generateToken(username);
  //
  //    return ResponseEntity.ok(Map.of("token", newAccessToken));
  //  }

  @GetMapping("/{username}")
  public ResponseEntity<UserAccountResponse> getUserAccount(
      @PathVariable String username, Authentication authentication) {
    log.info("get user account {}", username);
    String currentUsername = authentication.getName();
    log.info("current username {}", currentUsername);
    boolean isAdmin =
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

    if (!isAdmin && !username.equals(currentUsername)) {
      throw new AccessDeniedCustomException("You do not have permission to access this resource.");
    }

    UserAccount userAccount =
        userAccountService
            .findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

    return ResponseEntity.ok(UserAccountMapper.INSTANCE.toDTO(userAccount));
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserAccountResponse>> getAllUserAccounts() {
    List<UserAccount> userAccounts = userAccountService.findAll();
    log.info("getAllUserAccounts: {}", userAccounts);
    List<UserAccountResponse> userAccountResponses =
        userAccounts.stream()
            .map(UserAccountMapper.INSTANCE::toDTO)
            .peek(dto -> log.info("DTO: {}", dto))
            .toList();

    return ResponseEntity.ok(userAccountResponses);
  }
}
