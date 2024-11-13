package com.sparta.temueats.review.repository;

import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.store.entity.P_store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<P_review, UUID> {
    //가게 정보 가져오기
    @Query("SELECT r.store FROM P_review r WHERE r.store.storeId = :storeId")
    Optional<P_store> findStoreByStoreId(@Param("storeId") UUID storeId);
    //가게UUID로 리뷰리스트 가져오기
    @Query("SELECT r FROM P_review r WHERE r.store.storeId = :storeId")
    List<P_review> findByStoreId(@Param("storeId") UUID storeId);

    List<P_review> findByUserId(Long userId);

}
