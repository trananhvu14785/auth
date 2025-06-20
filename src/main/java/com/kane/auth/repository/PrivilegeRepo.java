package com.kane.auth.repository;

import com.kane.auth.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepo extends JpaRepository<Privilege, Integer> {
    Optional<Privilege> findByName(final String privilegeName);
}
