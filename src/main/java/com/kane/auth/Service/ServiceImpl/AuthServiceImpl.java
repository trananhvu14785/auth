package com.kane.auth.Service.ServiceImpl;

import com.kane.auth.Service.AuthService;
import com.kane.auth.model.ClientDetails;
import com.kane.auth.model.UserAccount;
import com.kane.auth.repository.ClientDetailsRepo;
import com.kane.auth.repository.UserAccountRepo;
import com.kane.common.dto.request.SignInRequest;
import com.kane.common.dto.response.SignInResponse;
import com.kane.common.exception.AuthException;
import com.kane.common.response.ErrorCode;
import com.kane.common.util.JwtUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final ClientDetailsRepo clientDetailsRepo;
  private final UserAccountRepo userAccountRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;

  @Override
  public SignInResponse signIn(SignInRequest signInRequest) {
    ClientDetails clientDetails =
        clientDetailsRepo
            .findByClientId(signInRequest.getClientId())
            .orElseThrow(() -> new AuthException("Client not found", ErrorCode.NOT_FOUND, "auth"));

    if (clientDetails.getSecretRequired()
        && !clientDetails.getClientSecret().equals(signInRequest.getClientSecret())) {
      throw new AuthException("Invalid client secret", ErrorCode.UNAUTHORIZED, "auth");
    }

    UserAccount user =
        userAccountRepo
            .findByUsername(signInRequest.getUsername())
            .orElseThrow(() -> new AuthException("User not found", ErrorCode.NOT_FOUND, "auth"));

    if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
      throw new AuthException("Invalid password", ErrorCode.UNAUTHORIZED, "auth");
    }
    List<String> roles =
        user.getProfile().getProfilePrivileges().stream()
            .map(pp -> pp.getPrivilege().getName())
            .collect(Collectors.toList());

    String token = jwtUtils.generateToken(user.getUsername(), roles);
    String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

    return SignInResponse.builder()
        .token(token)
        .refreshToken(refreshToken)
        .expiredDate(jwtUtils.extractExpiration(token))
        .build();
  }
}
