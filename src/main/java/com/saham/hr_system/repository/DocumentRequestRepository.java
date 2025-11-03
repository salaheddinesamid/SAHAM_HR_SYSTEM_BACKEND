package com.saham.hr_system.repository;

import com.saham.hr_system.model.DocumentRequest;
import com.saham.hr_system.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest,Long> {

    List<DocumentRequest> findAllByEmployee(Employee employee);
}
