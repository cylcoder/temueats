package com.sparta.temueats.store.repository;

import com.sparta.temueats.store.entity.P_storeReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreReqRepository extends JpaRepository<P_storeReq, UUID> {

}
