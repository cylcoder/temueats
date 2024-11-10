package com.sparta.temueats.user.repository;

import com.sparta.temueats.user.entity.P_user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<P_user, Long> {
    Optional<Object> findByEmail(String email);

    Optional<Object> findByNickname(String nickname);
}
