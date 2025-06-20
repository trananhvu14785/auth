package com.kane.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ProfilePrivilegeKey implements Serializable {
    @Column(name = "profile_id")
    private Integer profileId;

    @Column(name = "privilege_id")
    private Integer privilegeId;
}
