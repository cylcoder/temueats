package com.sparta.temueats.rating.repository;

import com.sparta.temueats.rating.entity.P_rating;
import com.sparta.temueats.store.entity.P_store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<P_rating, UUID> {

    P_rating findByStoreAndVisibleYn(P_store store, boolean visibleYn);

}
