package com.kane.auth.security;

import com.kane.auth.model.Privilege;
import com.kane.auth.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;
    private final Set<Privilege> roles;

    public CustomUserDetails(final UserAccount userAccount) {
        this.username = userAccount.getUsername();
        this.password = userAccount.getPassword();
        this.authorities = userAccount.getProfile().getProfilePrivileges().stream()
                .map(profilePrivilege -> (GrantedAuthority) () -> profilePrivilege.getPrivilege().getName())
                .collect(java.util.stream.Collectors.toSet());
        this.roles = userAccount.getProfile().getProfilePrivileges().stream()
                .map(profilePrivilege -> profilePrivilege.getPrivilege())
                .collect(java.util.stream.Collectors.toSet());
    }

    // Override lại các phương thức từ interface UserDetails của Spring Security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {return this.password; }

    @Override
    public String getUsername() {return this.username; }
}
