package com.sparta.temueats.review.repository;

import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.store.entity.P_store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<P_review, UUID> {

//    P_store findByStore(Long storeId);
}
