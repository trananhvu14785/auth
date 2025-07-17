package com.kane.auth.repository;

import com.kane.auth.model.ClientDetails;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailsRepo extends JpaRepository<ClientDetails, Integer> {
  Optional<ClientDetails> findByClientId(final String clientId);
}
