package com.sparta.temueats.store.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.store.dto.StoreReqCreateDto;
import com.sparta.temueats.store.dto.StoreReqResDto;
import com.sparta.temueats.store.dto.StoreReqUpdateDto;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.P_storeReq;
import com.sparta.temueats.store.entity.StoreState;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.store.repository.StoreReqRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.temueats.global.ResponseDto.FAILURE;
import static com.sparta.temueats.global.ResponseDto.SUCCESS;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreReqService {

    private final StoreReqRepository storeReqRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;

    public ResponseDto<StoreReqResDto> save(StoreReqCreateDto storeReqCreateDto, HttpServletRequest req) {
        Optional<P_user> userOptional = userService.validateTokenAndGetUser(req);
        if (userOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다.");
        }

        if (!storeRepository.findByName(storeReqCreateDto.getName()).isEmpty()) {
            return new ResponseDto<>(FAILURE, "이미 존재하는 가게명입니다.");
        }

        P_user user = userOptional.get();
        P_storeReq storeReq = storeReqCreateDto.toEntity(user);
        storeReq.setCreatedBy(user.getNickname());
        storeReqRepository.save(storeReq);
        return new ResponseDto<>(SUCCESS, "가게 등록 요청 성공", new StoreReqResDto(storeReq));
    }

    public ResponseDto<Object> update(StoreReqUpdateDto storeReqUpdateDto, HttpServletRequest req) {
        Optional<P_user> userOptional = userService.validateTokenAndGetUser(req);
        if (userOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다.");
        }

        Optional<P_storeReq> storeReqOptional = storeReqRepository.findById(storeReqUpdateDto.getStoreReqId());
        if (storeReqOptional.isEmpty()) {
            return  new ResponseDto<>(FAILURE,"존재하지 않는 가게 등록 요청입니다.");
        }

        P_user user = userOptional.get();

        P_storeReq storeReq = storeReqOptional.get();
        storeReq.update(storeReqUpdateDto.getStoreReqState());
        storeReq.setUpdatedBy(user.getNickname());

        P_store store = toStore(storeReq);
        store.setCreatedBy(user.getNickname());
        storeRepository.save(store);

        return new ResponseDto<>(SUCCESS, "가게 요청 상태 수정 완료");
    }

    private P_store toStore(P_storeReq storeReq) {
        return P_store.builder()
                .user(storeReq.getRequestedBy())
                .name(storeReq.getName())
                .image(storeReq.getImage())
                .number(storeReq.getNumber())
                .leastPrice(storeReq.getLeastPrice())
                .deliveryPrice(storeReq.getDeliveryPrice())
                .category(storeReq.getCategory())
                .latLng(storeReq.getLatLng())
                .address(storeReq.getAddress())
                .state(StoreState.CLOSED)
                .build();
    }

}
