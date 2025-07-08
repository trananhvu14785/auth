package com.kane.auth.repository;

import com.kane.auth.model.UserAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, Integer> {
  Optional<UserAccount> findByUsername(final String username);
}
