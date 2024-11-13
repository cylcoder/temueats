package com.sparta.temueats.menu.repository;

import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.store.entity.P_store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<P_menu, UUID> {

    Optional<P_menu> findByName(String name);

    List<P_menu> findByStore(P_store store);

}
