package com.kane.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRQ {
  @Email(message = "Email must be valid")
  @NotBlank(message = "Email is required")
  private String email;
}
