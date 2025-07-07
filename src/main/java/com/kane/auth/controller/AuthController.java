package com.kane.auth.controller;

import com.kane.auth.Mapper.UserAccountMapper;
import com.kane.auth.Service.UserAccountService;
//import com.kane.auth.dto.request.SignUpRequest;
//import com.kane.auth.dto.response.SignInResponse;
import com.kane.auth.dto.request.SignUpRequest;
import com.kane.auth.dto.response.SignInResponse;
import com.kane.auth.model.UserAccount;
import com.kane.auth.security.CustomUserDetails;
import com.kane.auth.security.CustomUserDetailsService;
import com.kane.auth.util.JwtUtils;
import com.kane.common.dto.request.SignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserAccountService userAccountService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

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
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signIpRequest.getUsername(), signIpRequest.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String accessToken = this.jwtUtils.generateToken(userDetails);
            Date expiredDate = this.jwtUtils.extractExpiration(accessToken);
            String refreshToken = this.jwtUtils.generateRefreshToken(userDetails);
            return ResponseEntity.ok(SignInResponse.builder()
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
    public  ResponseEntity<?> refeshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (!StringUtils.hasText(refreshToken) || jwtUtils.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is invalid or expired");
        }

        String username = jwtUtils.extractUsername(refreshToken);
        var userDetails =  customUserDetailsService.loadUserByUserName(username);
        var newAccessToken = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", newAccessToken));
    }
}
