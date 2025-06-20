package com.kane.auth.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SignInRequest {
    private String username;
    private String password;
}
