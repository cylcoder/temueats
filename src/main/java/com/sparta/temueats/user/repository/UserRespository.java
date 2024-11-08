package com.sparta.temueats.user.repository;

import com.sparta.temueats.user.entity.P_user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<P_user, Long> {
}
