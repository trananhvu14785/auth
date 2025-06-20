package com.kane.auth.controller;

import com.kane.auth.Mapper.UserAccountMapper;
import com.kane.auth.Service.UserAccountService;
import com.kane.auth.dto.request.SignUpRequest;
import com.kane.auth.dto.response.SignInResponse;
import com.kane.auth.model.UserAccount;
import com.kane.auth.security.CustomUserDetails;
import com.kane.auth.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserAccountService userAccountService;
    private final AuthenticationManager authenticationManager;
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
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignUpRequest signUpRequest) {
        try {
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(), signUpRequest.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String accessToken = this.jwtUtils.generateToken(userDetails);
            Date expiredDate = this.jwtUtils.extractExpiration(accessToken);
            return ResponseEntity.ok(SignInResponse.builder()
                    .token(accessToken)
                    .expiredDate(expiredDate)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password", e);
        }
    }
}
