package com.kane.auth.Service.ServiceImpl;

import com.kane.auth.Service.UserAccountService;
import com.kane.auth.constant.SecurityRole;
import com.kane.auth.model.*;
import com.kane.auth.repository.PrivilegeRepo;
import com.kane.auth.repository.UserAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserAccountImpl implements UserAccountService {
    private final UserAccountRepo userAccountRepo;
    private final PrivilegeRepo privilegeRepo;
    private final PasswordEncoder passwordEncoder;

//    public UserAccountImpl(UserAccountRepo userAccountRepo, PrivilegeRepo privilegeRepo) {
//        this.userAccountRepo = userAccountRepo;
//        this.privilegeRepo = privilegeRepo;
//    }

    @Override
    public Boolean save(UserAccount userAccount) {
        this.userAccountRepo.findByUsername(userAccount.getUsername()).ifPresent(user -> {
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        });

        userAccount.setPassword(this.passwordEncoder.encode(userAccount.getPassword()));

        Privilege userPrivilege = privilegeRepo.findByName(SecurityRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("No Privilege with name " + SecurityRole.ROLE_USER));
        Profile userProfile = userAccount.getProfile();

        if (userProfile.getId() == null) {
            userAccountRepo.save(userAccount);
        }

        ProfilePrivilege profilePrivilege = ProfilePrivilege.builder()
                .profile(userProfile)
                .privilege(userPrivilege)
                .key(ProfilePrivilegeKey.builder()
                        .profileId(userProfile.getId())
                        .privilegeId(userPrivilege.getId())
                        .build())
                .build();

        if (userProfile.getProfilePrivileges() == null) {
            userProfile.setProfilePrivileges(new ArrayList<>());
        }

        userProfile.getProfilePrivileges().add(profilePrivilege);

        this.userAccountRepo.save(userAccount);
        return true;
    }
}
