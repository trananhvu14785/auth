package com.kane.auth.Service;

import com.kane.common.dto.request.SignInRequest;
import com.kane.common.dto.response.SignInResponse;

public interface AuthService {
  public SignInResponse signIn(final SignInRequest signInRequest);
}
