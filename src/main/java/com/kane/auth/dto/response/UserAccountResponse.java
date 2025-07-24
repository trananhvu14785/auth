package com.kane.auth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountResponse {
  private String username;
  private String name;
  private boolean active;
}
