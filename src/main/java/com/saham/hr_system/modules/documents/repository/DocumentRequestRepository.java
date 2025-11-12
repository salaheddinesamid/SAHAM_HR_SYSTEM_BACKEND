package com.saham.hr_system.modules.documents.repository;

import com.saham.hr_system.modules.documents.model.DocumentRequest;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest,Long> {

    List<DocumentRequest> findAllByEmployee(Employee employee);
}
