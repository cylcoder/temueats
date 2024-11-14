package com.sparta.temueats.store.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.repository.MenuRepository;
import com.sparta.temueats.rating.repository.RatingRepository;
import com.sparta.temueats.review.dto.response.ReviewResDto;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.repository.ReviewRepository;
import com.sparta.temueats.store.dto.*;
import com.sparta.temueats.store.entity.P_favStore;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.FavStoreRepository;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.temueats.global.ResponseDto.FAILURE;
import static com.sparta.temueats.global.ResponseDto.SUCCESS;
import static java.util.Collections.reverse;
import static java.util.Collections.reverseOrder;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final FavStoreRepository favStoreRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final RatingRepository ratingRepository;

    public ResponseDto<Object> update(StoreUpdateDto storeUpdateDto) {
        P_user user = userService.getUser();

        Optional<P_store> storeOptional = storeRepository.findById(storeUpdateDto.getStoreId());
        if (storeOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "존재하지 않는 가게입니다.");
        }

        P_store store = storeOptional.get();
        store.update(storeUpdateDto, user);
        return new ResponseDto<>(SUCCESS, "가게 정보 수정 성공");
    }

    public ResponseDto<List<StoreResDto>> findByStoreNameContaining(
            String storeName, String order, String orderBy, int page, int size) {
        Long userId = userService.getUser().getId();
        Pageable pageable = PageRequest.of(page, size);

        List<StoreResDto> stores;
        if ("createdAt".equals(orderBy)) {
            stores = storeRepository.findByStoreNameContainingOrderByCreatedAtDesc(storeName, userId, pageable);
        } else {
            stores = storeRepository.findByStoreNameContainingOrderByUpdatedAtDesc(storeName, userId, pageable);
        }

        if ("asc".equals(order)) {
            reverse(stores);
        }

        if (stores.isEmpty()) {
            return new ResponseDto<>(SUCCESS, storeName + "와(과) 일치하는 검색결과가 없습니다.");
        }

        return new ResponseDto<>(SUCCESS, "가게 검색 성공", stores);
    }

    public ResponseDto<List<StoreResDto>> findByMenuNameContaining(
            String menuName, String order, String sortBy, int page, int size) {
        Long userId = userService.getUser().getId();
        Pageable pageable = PageRequest.of(page, size);

        List<StoreResDto> stores;
        if ("createdAt".equals(sortBy)) {
            stores = storeRepository.findByMenuNameContainingOrderByCreatedAtDesc(menuName, userId, pageable);
        } else {
            stores = storeRepository.findByMenuNameContainingOrderByUpdatedAtDesc(menuName, userId, pageable);
        }

        if ("asc".equals(order)) {
            reverse(stores);
        }

        if (stores.isEmpty()) {
            return new ResponseDto<>(SUCCESS, menuName + "와(과) 일치하는 검색결과가 없습니다.");
        }

        return new ResponseDto<>(SUCCESS, "가게 검색 성공", stores);
    }

    public Optional<P_store> findEntityById(UUID storeId) {
        return storeRepository.findById(storeId);
    }

    public ResponseDto<StoreDetailResDto> findDetailById(UUID storeId) {
        P_user user = userService.getUser();

        Optional<P_store> storeOptional = storeRepository.findById(storeId);
        if (storeOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "존재하지 않는 가게 번호입니다.");
        }

        P_store store = storeOptional.get();

        StoreDetailResDto storeDetailResDto = new StoreDetailResDto(store);
        List<P_review> reviews = reviewRepository.findByStoreId(storeId);
        storeDetailResDto.setReviewCount(reviews.size());
        storeDetailResDto.setIsFavorite(favStoreRepository.findByUserAndStore(user, store).isPresent());
        storeDetailResDto.setMenu(menuRepository.findByStore(store).stream().map(MenuResDto::new).toList());
        storeDetailResDto.setReviews(reviews.stream().map(ReviewResDto::new).toList());
        storeDetailResDto.setRating(ratingRepository.findByStoreAndVisibleYn(store, true).getScore());
        return new ResponseDto<>(SUCCESS, "가게 상세 조회 성공", storeDetailResDto);
    }

    public ResponseDto<Object> delete(UUID storeId) {
        Optional<P_store> storeOptional = storeRepository.findById(storeId);
        if (storeOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "존재하지 않는 가게 번호입니다.");
        }

        storeOptional.get().delete();
        return new ResponseDto<>(SUCCESS, "가게 삭제 성공");
    }

    // 가게 즐겨찾기 추가/삭제
    public ResponseDto addFavStore(AddFavStoreRequestDto requestDto) {

        P_user user = userService.getUser();

        // 가게 검증
        P_store store = findEntityById(requestDto.getStoreId()).orElse(null);
        if(store == null) {
            return new ResponseDto<>(-1, "존재하지 않는 가게입니다", null);
        }
        // 즐겨찾기 여부 확인
        Optional<P_favStore> existingFavStore = favStoreRepository.findByUserAndStore(user, store);

        if (existingFavStore.isPresent()) {
            // 이미 즐겨찾기에 있다면 삭제
            favStoreRepository.delete(existingFavStore.get());
            return new ResponseDto<>(1, "즐겨찾기에서 삭제되었습니다", null);
        } else {
            // 즐겨찾기에 없다면 추가
            P_favStore favStore = P_favStore.builder()
                    .user(user)
                    .store(store)
                    .build();
            favStoreRepository.save(favStore);
            return new ResponseDto<>(1, "즐겨찾기에 추가되었습니다", null);
        }

    }


    // 즐겨찾기 가게 목록 조회
    public ResponseDto getFavStoreList() {
        // 사용자 검증
        P_user user = userService.getUser();

        // 즐겨찾기 가게 목록 조회
        List<P_store> favStores = favStoreRepository.findByUser(user);
        List<FavStoreListResponseDto> stores = favStores.stream()
                .map(FavStoreListResponseDto::new)
                .toList();

        return new ResponseDto(1, "즐겨찾기 가게 목록 조회 성공", stores);
    }

}
