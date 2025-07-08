package com.kane.auth.response;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
  private final OperationType operationType = OperationType.FAILURE;
  private final String message;
  private ErrorCode code;
  private String domain;
  private Map<String, Object> details;
  private final OffsetDateTime timestamp = OffsetDateTime.now();

  public static ErrorResponse of(final ErrorCode code, final String domain) {
    return ErrorResponse.builder().code(code).domain(domain).build();
  }

  public static ErrorResponse of(final ErrorCode code, final String domain, final String message) {
    return ErrorResponse.builder().code(code).domain(domain).message(message).build();
  }

  public static ErrorResponse of(
      final ErrorCode code,
      final String domain,
      final String message,
      final Map<String, Object> details) {
    return ErrorResponse.builder()
        .code(code)
        .domain(domain)
        .message(message)
        .details(details)
        .build();
  }
}
