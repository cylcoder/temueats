package com.sparta.temueats.cart.repository;

import com.sparta.temueats.cart.entity.P_cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<P_cart, UUID> {

    @Query("select c from P_cart c where c.user.id = :userId AND c.deletedYn = false")
    List<P_cart> findAllByUserId(Long userId);


    @Query("select c from P_cart c where c.menu.menuId = :menuId AND c.user.id = :userId AND c.deletedYn = false")
    P_cart findByMenuIdByUserId(UUID menuId, Long userId);

    @Query("select c from P_cart c where c.selectYn = true AND c.user.id = :userId AND c.deletedYn = false")
    List<P_cart> findAllBySelectAndUserId(Long userId);
}
