package com.sparta.temueats.report.repository;

import com.sparta.temueats.report.entity.P_report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<P_report, UUID> {
}
