package com.kane.auth.response;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse<T> {
  private final OperationType operationType = OperationType.SUCCESS;
  private final String message = "Success";
  private ErrorCode code;
  private T data;
  private final OffsetDateTime timestamp = OffsetDateTime.now();
  private int size;
  private int page;

  public static <T> SuccessResponse<T> of(T data) {
    return SuccessResponse.<T>builder().data(data).code(ErrorCode.OK).size(getSize(data)).build();
  }

  public static <T> SuccessResponse<T> of(T data, final int page) {
    return SuccessResponse.<T>builder()
        .data(data)
        .code(ErrorCode.OK)
        .size(getSize(data))
        .page(page)
        .build();
  }

  private static <T> int getSize(T data) {
    if (Objects.isNull(data) && data instanceof Collection<?>) {
      return ((Collection<?>) data).size();
    }
    return 0;
  }
}
