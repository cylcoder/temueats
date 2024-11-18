package com.sparta.temueats.rating.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.rating.dto.response.GetRating;
import com.sparta.temueats.rating.dto.response.GetRatingRes;
import com.sparta.temueats.rating.entity.P_rating;
import com.sparta.temueats.rating.repository.RatingRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final StoreRepository storeRepository;

    public GetRatingRes getRating(UUID storeId){
        P_rating rating=ratingRepository.findByStore_StoreId(storeId);
        if(rating==null){
            return GetRatingRes.builder()
                    .code(-1)
                    .message("가게 평점이 존재하지 않습니다.")
                    .getRating(null)
                    .build();
        }
        return GetRatingRes.builder()
                .code(1)
                .message("가게 평점 조회")
                .getRating(GetRating.builder().rating(rating.getScore()).build())
                .build();

    }
    @Transactional
    public void saveRating(UUID storeId,int score){
        P_store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomApiException("해당 가게는 없습니다."));
        P_rating rating=ratingRepository.findByStore_StoreId(storeId);
        if(rating==null){
            rating=P_rating.builder()
                    .sum(0)
                    .score(0.0)
                    .store(store)
                    .count(0)
                    .visibleYn(true)
                    .build();
        }
        rating.changeSum(score);
        ratingRepository.save(rating);
    }
}
