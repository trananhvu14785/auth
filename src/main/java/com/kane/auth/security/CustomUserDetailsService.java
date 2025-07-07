package com.kane.auth.security;

import com.kane.auth.model.UserAccount;
import com.kane.auth.repository.UserAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccountRepo userAccountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = this.userAccountRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: " + username));

        return new CustomUserDetails(userAccount);
    }

    public CustomUserDetails loadUserByUserName(String username) throws UsernameNotFoundException {
        UserAccount userAccount = this.userAccountRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: " + username));

        return new CustomUserDetails(userAccount);
    }
}
