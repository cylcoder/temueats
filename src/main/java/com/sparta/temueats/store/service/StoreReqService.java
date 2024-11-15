package com.sparta.temueats.store.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.rating.entity.P_rating;
import com.sparta.temueats.rating.repository.RatingRepository;
import com.sparta.temueats.s3.service.FileService;
import com.sparta.temueats.store.dto.StoreReqCreateDto;
import com.sparta.temueats.store.dto.StoreReqCreateWithImageDto;
import com.sparta.temueats.store.dto.StoreReqResDto;
import com.sparta.temueats.store.dto.StoreReqUpdateDto;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.P_storeReq;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.store.repository.StoreReqRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.temueats.global.ResponseDto.FAILURE;
import static com.sparta.temueats.global.ResponseDto.SUCCESS;
import static com.sparta.temueats.store.entity.StoreState.CLOSED;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreReqService {

    private final StoreReqRepository storeReqRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;
    private final FileService fileService;
    private final RatingRepository ratingRepository;

    public ResponseDto<StoreReqResDto> save(StoreReqCreateDto storeReqCreateDto) {
        P_user user = userService.getUser();

        if (!storeRepository.findByName(storeReqCreateDto.getName()).isEmpty()) {
            return new ResponseDto<>(FAILURE, "이미 존재하는 가게명입니다.");
        }

        P_storeReq storeReq = storeReqCreateDto.toEntity(user);
        storeReqRepository.save(storeReq);
        return new ResponseDto<>(SUCCESS, "가게 등록 요청 성공", new StoreReqResDto(storeReq));
    }

    public ResponseDto<StoreReqResDto> save(
            StoreReqCreateWithImageDto storeReqCreateWithImageDto
           ) {
        P_user user = userService.getUser();

        if (!storeRepository.findByName(storeReqCreateWithImageDto.getName()).isEmpty()) {
            return new ResponseDto<>(FAILURE, "이미 존재하는 가게명입니다.");
        }

        String image = null;
        if (storeReqCreateWithImageDto.getImage() != null && !storeReqCreateWithImageDto.getImage().isEmpty()) {
            try {
                image = fileService.uploadFile(storeReqCreateWithImageDto.getImage());
            } catch (IOException e) {
                return new ResponseDto<>(ResponseDto.FAILURE, "이미지 업로드 실패");
            }
        }

        P_storeReq storeReq = storeReqCreateWithImageDto.toEntity(user, image);
        storeReq.setCreatedBy(user.getNickname());
        StoreReqResDto storeReqResDto = new StoreReqResDto(storeReqRepository.save(storeReq));
        return new ResponseDto<>(SUCCESS, "가게 등록 요청 성공", storeReqResDto);
    }

    public ResponseDto<Object> update(StoreReqUpdateDto storeReqUpdateDto) {
        P_user user = userService.getUser();

        Optional<P_storeReq> storeReqOptional = storeReqRepository.findById(storeReqUpdateDto.getStoreReqId());
        if (storeReqOptional.isEmpty()) {
            return  new ResponseDto<>(FAILURE,"존재하지 않는 가게 등록 요청입니다.");
        }

        String creator = user.getNickname();

        P_storeReq storeReq = storeReqOptional.get();
        storeReq.update(storeReqUpdateDto.getStoreReqState());
        storeReq.setUpdatedBy(creator);

        P_store store = toStore(storeReq);
        store.setCreatedBy(creator);
        storeRepository.save(store);

        P_rating rating = P_rating.builder()
                .store(store)
                .score(0.0)
                .visibleYn(true)
                .build();
        rating.setCreatedBy(creator);
        ratingRepository.save(rating);

        return new ResponseDto<>(SUCCESS, "가게 요청 상태 수정 완료");
    }

    public ResponseDto<Object> delete(UUID storeReqId) {
        Optional<P_storeReq> storeReqOptional = storeReqRepository.findById(storeReqId);
        if (storeReqOptional.isEmpty()) {
            return  new ResponseDto<>(FAILURE,"존재하지 않는 가게 삭제 요청입니다.");
        }

        storeReqOptional.get().delete();
        return new ResponseDto<>(SUCCESS, "가게 등록 요청 취소 완료");
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
                .state(CLOSED)
                .build();
    }

}
