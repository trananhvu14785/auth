package com.kane.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "user_account")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, columnDefinition = "bit default 1")
  private Boolean active;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "profile_id", referencedColumnName = "id")
  private Profile profile;
}
