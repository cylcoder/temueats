package com.sparta.temueats.store.repository;

import com.sparta.temueats.store.entity.P_favStore;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.user.entity.P_user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavStoreRepository extends JpaRepository<P_favStore, UUID> {
    Optional<P_favStore> findByUserAndStore(P_user user, P_store store);

    @Query("SELECT f.store FROM P_FAV_STORE f WHERE f.user = :user ORDER BY f.createdAt DESC")
    List<P_store> findByUser(P_user user);
}
