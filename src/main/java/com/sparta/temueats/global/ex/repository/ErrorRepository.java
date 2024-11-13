package com.sparta.temueats.global.ex.repository;

import com.sparta.temueats.global.ex.entity.P_error;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ErrorRepository extends JpaRepository<P_error, UUID> {

}
