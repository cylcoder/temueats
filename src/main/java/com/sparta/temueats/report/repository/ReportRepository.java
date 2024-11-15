package com.sparta.temueats.report.repository;

import com.sparta.temueats.report.entity.P_report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<P_report, UUID> {
    Page<P_report> findAll(Pageable pageable);

    Page<P_report> findByUserId(Long userId, Pageable pageable);

    Page<P_report> findByStoreName(String name, Pageable pageable);
}
