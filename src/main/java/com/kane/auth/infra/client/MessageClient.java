package com.kane.auth.infra.client;

import com.kane.auth.dto.UserRegisteredEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "message-service", url = "${message-service.url}")
public interface MessageClient {
  @PostMapping("/messages/event")
  ResponseEntity<String> event(@RequestBody UserRegisteredEvent userRegisteredEvent);
}
