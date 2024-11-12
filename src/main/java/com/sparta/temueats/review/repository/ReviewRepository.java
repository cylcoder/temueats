package com.sparta.temueats.review.repository;

import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.store.entity.P_store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<P_review, UUID> {

//    P_store findByStore(Long storeId);

    @Query("SELECT COUNT(r) FROM P_review r WHERE r.store.storeId = :storeId AND r.useYn = true")
    Integer countReviewsByStoreId(UUID storeId);

}
