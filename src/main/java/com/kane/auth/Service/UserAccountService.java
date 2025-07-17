package com.kane.auth.Service;

import com.kane.auth.model.UserAccount;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {
  Boolean save(final UserAccount userAccount);

  Optional<UserAccount> findByUsername(final String username);

  List<UserAccount> findAll();
}
