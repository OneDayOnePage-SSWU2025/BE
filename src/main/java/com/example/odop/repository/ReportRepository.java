package com.example.odop.repository;

import com.example.odop.entity.Reports;
import com.example.odop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Reports, Long> {
    Optional<Reports> findByReportId(Long reportId);
    Optional<Reports> findByUser(Users user);

}
