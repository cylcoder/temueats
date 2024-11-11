//package com.sparta.temueats.store.service;
//
//import com.sparta.temueats.global.ex.CustomApiException;
//import com.sparta.temueats.store.dto.StoreReqCreateDto;
//import com.sparta.temueats.store.dto.StoreReqUpdateDto;
////import com.sparta.temueats.store.dto.StoreReqResDto;
//import com.sparta.temueats.store.entity.P_store;
//import com.sparta.temueats.store.entity.P_storeReq;
//import com.sparta.temueats.store.entity.StoreState;
//import com.sparta.temueats.store.repository.StoreRepository;
//import com.sparta.temueats.store.repository.StoreReqRepository;
//import com.sparta.temueats.user.entity.P_user;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class StoreReqService {
//
//    private final StoreReqRepository storeReqRepository;
//    private final StoreRepository storeRepository;
//
////    public StoreReqResDto saveStoreReq(StoreReqCreateDto storeReqCreateDto, P_user user) {
////        P_storeReq storeReq = storeReqCreateDto.toEntity(user);
////        storeReq.setCreatedBy(user.getNickname());
////        return new StoreReqResDto(storeReqRepository.save(storeReq));
////    }
//
//    public void updateState(StoreReqUpdateDto storeReqUpdateDto, P_user user) {
//        Optional<P_storeReq> storeReqOptional = storeReqRepository.findById(storeReqUpdateDto.getStoreReqId());
//
//        if (storeReqOptional.isEmpty()) {
//            throw new CustomApiException("존재하지 않는 가게 등록 요청");
//        }
//
//        P_storeReq storeReq = storeReqOptional.get();
//        storeReq.updateState(storeReqUpdateDto.getStoreReqState());
//        storeReq.setUpdatedBy(user.getNickname());
//
//        P_store store = toStore(storeReq);
//        store.setCreatedBy(user.getNickname());
//        storeRepository.save(store);
//    }
//
//    private P_store toStore(P_storeReq storeReq) {
//        return P_store.builder()
//                .user(storeReq.getRequestedBy())
//                .name(storeReq.getName())
//                .image(storeReq.getImage())
//                .number(storeReq.getNumber())
//                .leastPrice(storeReq.getLeastPrice())
//                .deliveryPrice(storeReq.getDeliveryPrice())
//                .category(storeReq.getCategory())
//                .latLng(storeReq.getLatLng())
//                .address(storeReq.getAddress())
//                .state(StoreState.CLOSED)
//                .build();
//    }
//
//}
