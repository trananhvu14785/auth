package com.kane.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "profile_privilege")
public class ProfilePrivilege {
  @EmbeddedId private ProfilePrivilegeKey key;

  @ManyToOne
  @MapsId("profileId")
  @JoinColumn(name = "profile_id")
  private Profile profile;

  @ManyToOne
  @MapsId("privilegeId")
  @JoinColumn(name = "privilege_id")
  private Privilege privilege;
}
