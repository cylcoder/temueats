package com.sparta.temueats.store.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.store.dto.StoreResDto;
import com.sparta.temueats.store.dto.StoreUpdateDto;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;

    public void update(StoreUpdateDto storeUpdateDto, P_user user) {
        storeRepository.findById(storeUpdateDto.getStoreId())
                .orElseThrow(() -> new CustomApiException("존재하지 않는 음식점입니다."))
                .update(storeUpdateDto, user);

    }

    public List<StoreResDto> findByName(String name) {
        return storeRepository.findByNameContaining(name);
    }

    public Optional<P_store> findById(UUID storeId) {
        return storeRepository.findById(storeId);
    }

}
