package com.saham.hr_system.modules.employees.repository;

import com.saham.hr_system.modules.employees.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String name);
}
