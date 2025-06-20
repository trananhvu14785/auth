package com.kane.auth.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponse {
    private String token;
    private Date expiredDate;
}
