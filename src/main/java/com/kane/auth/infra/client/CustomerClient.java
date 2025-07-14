package com.kane.auth.infra.client;

import com.kane.common.dto.request.CustomerResponse;
import com.kane.common.response.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${customer-service.url}")
public interface CustomerClient {
  @GetMapping("/customer/email/{email}")
  ResponseEntity<SuccessResponse<CustomerResponse>> getCustomerByEmail(@PathVariable String email);
}
