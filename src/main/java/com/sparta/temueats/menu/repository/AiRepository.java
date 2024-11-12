package com.sparta.temueats.menu.repository;

import com.sparta.temueats.menu.entity.P_aiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AiRepository extends JpaRepository<P_aiLog, UUID> {

}
