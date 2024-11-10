package com.sparta.temueats.store.service;

import com.sparta.temueats.store.dto.StoreReqDto;
import com.sparta.temueats.store.dto.StoreResDto;
import com.sparta.temueats.store.entity.P_storeReq;
import com.sparta.temueats.store.repository.StoreReqRepository;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreReqRepository storeReqRepository;

    public StoreResDto saveStoreReq(StoreReqDto storeReqDto, P_user user) {
        P_storeReq storeReq = storeReqDto.toEntity(user);
        storeReq.setCreatedBy(user.getNickname());
        return new StoreResDto(storeReqRepository.save(storeReq));
    }

}
