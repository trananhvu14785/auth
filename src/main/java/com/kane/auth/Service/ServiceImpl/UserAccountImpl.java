package com.kane.auth.Service.ServiceImpl;

import com.kane.auth.Service.UserAccountService;
// import com.kane.auth.constant.SecurityRole;
import com.kane.auth.infra.client.CustomerClient;
import com.kane.auth.model.*;
import com.kane.auth.repository.PrivilegeRepo;
import com.kane.auth.repository.UserAccountRepo;
import com.kane.common.constant.SecurityRole;
import com.kane.common.dto.request.CustomerResponse;
import com.kane.common.exception.NoFoundException;
import com.kane.common.response.SuccessResponse;
import feign.FeignException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountImpl implements UserAccountService {
  private final CustomerClient customerClient;
  private final UserAccountRepo userAccountRepo;
  private final PrivilegeRepo privilegeRepo;
  private final PasswordEncoder passwordEncoder;

  //    Logger logger = Logger.getLogger(UserAccountImpl.class.getName());

  @Override
  public Boolean save(UserAccount userAccount) {
    try {
      ResponseEntity<SuccessResponse<CustomerResponse>> customerResponse =
          this.customerClient.getCustomerByEmail(userAccount.getUsername());

      if (!customerResponse.getStatusCode().is2xxSuccessful()
          || customerResponse.getBody() == null
          || customerResponse.getBody().getData() == null) {
        throw new RuntimeException(
            "Customer with email " + userAccount.getUsername() + " not found");
      }

      CustomerResponse customer = customerResponse.getBody().getData();

      userAccount.setName(customer.getFirstName() + " " + customer.getLastName());
      userAccount.setPassword(this.passwordEncoder.encode(userAccount.getPassword()));
      userAccount.setActive(true);

      Privilege userPrivilege =
          privilegeRepo
              .findByName(SecurityRole.ROLE_USER)
              .orElseThrow(
                  () -> new RuntimeException("No Privilege with name " + SecurityRole.ROLE_USER));
      Profile userProfile = userAccount.getProfile();

      if (userProfile.getId() == null) {
        userAccountRepo.save(userAccount);
      }

      ProfilePrivilege profilePrivilege =
          ProfilePrivilege.builder()
              .profile(userProfile)
              .privilege(userPrivilege)
              .key(
                  ProfilePrivilegeKey.builder()
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
    } catch (FeignException.NotFound ex) {
      throw new NoFoundException("Customer with email " + userAccount.getUsername() + " not found");
    }
  }

  @Override
  public Optional<UserAccount> findByUsername(String username) {
    return this.userAccountRepo.findByUsername(username);
  }

  @Override
  public List<UserAccount> findAll() {
    List<UserAccount> list = userAccountRepo.findAll();
    for (UserAccount user : list) {
      log.info(
          "UserAccount: username={}, active={}, profileId={}",
          user.getUsername(),
          user.getActive(),
          user.getProfile() != null ? user.getProfile().getId() : null);
    }
    return list;
  }
}
