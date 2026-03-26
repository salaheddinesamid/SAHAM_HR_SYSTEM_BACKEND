package com.saham.hr_system.modules.auth.repository;

import com.saham.hr_system.modules.auth.model.PasswordSetupToken;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordSetupTokenRepository extends JpaRepository<PasswordSetupToken, Long> {

    Optional<PasswordSetupToken> findByToken(String token);

    Optional<PasswordSetupToken> findByEmployee(Employee employee);
}
