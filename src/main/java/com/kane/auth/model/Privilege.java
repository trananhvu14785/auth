package com.kane.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "privilege")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "privilege")
    private List<ProfilePrivilege> profilePrivileges = new ArrayList<>();
}
