package com.sparta.temueats.store.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.rating.repository.RatingRepository;
import com.sparta.temueats.store.dto.StoreReqCreateDto;
import com.sparta.temueats.store.dto.StoreReqResDto;
import com.sparta.temueats.store.dto.StoreReqUpdateDto;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.P_storeReq;
import com.sparta.temueats.store.entity.StoreReqState;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.store.repository.StoreReqRepository;
import com.sparta.temueats.store.util.GeoUtils;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.temueats.global.ResponseDto.SUCCESS;
import static com.sparta.temueats.menu.entity.Category.KOREAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Transactional
class StoreReqServiceTest {

    @InjectMocks
    private StoreReqService storeReqService;

    @Mock
    private StoreReqRepository storeReqRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserService userService;

    private P_user testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = P_user.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("testUser")
                .build();
        when(userService.getUser()).thenReturn(testUser);
    }

    @Test
    @DisplayName("사용자는 가게 등록 요청을 할 수 있다.")
    void testSaveWithoutImage() {
        StoreReqCreateDto createDto = StoreReqCreateDto.builder()
                .name("Test Store")
                .number("010-1234-5678")
                .leastPrice(1000)
                .deliveryPrice(3000)
                .category(KOREAN)
                .latitude(37.5665)
                .longitude(126.9780)
                .address("Seoul, South Korea")
                .build();

        when(storeRepository.findByName(createDto.getName())).thenReturn(Collections.emptyList());

        ResponseDto<StoreReqResDto> response = storeReqService.save(createDto);

        assertEquals(SUCCESS, response.getCode());
        assertEquals("가게 등록 요청 성공", response.getMsg());

        verify(storeReqRepository, times(1)).save(argThat(storeReq -> {
            assertEquals("Test Store", storeReq.getName());
            assertEquals("010-1234-5678", storeReq.getNumber());
            assertEquals(1000, storeReq.getLeastPrice());
            assertEquals(3000, storeReq.getDeliveryPrice());
            assertEquals(KOREAN, storeReq.getCategory());
            assertEquals(GeoUtils.toPoint(37.5665, 126.9780), storeReq.getLatLng());
            assertEquals("Seoul, South Korea", storeReq.getAddress());
            assertEquals(testUser, storeReq.getRequestedBy());
            return true;
        }));
    }

    @Test
    @DisplayName("매니저는 가게 등록 요청 건을 처리할 수 있다.")
    void testUpdateStoreReq_Success() {
        // Given
        UUID storeReqId = UUID.randomUUID();
        StoreReqUpdateDto updateDto = StoreReqUpdateDto.builder()
                .storeReqId(storeReqId)
                .storeReqState(StoreReqState.APPROVED)
                .build();

        P_storeReq storeReq = P_storeReq.builder()
                .storeReqId(storeReqId)
                .requestedBy(testUser)
                .state(StoreReqState.PENDING)
                .build();

        storeReqRepository.save(storeReq);
        when(storeReqRepository.findById(storeReqId)).thenReturn(Optional.of(storeReq));

        // When
        ResponseDto<Object> response = storeReqService.update(updateDto);

        // Then
        assertEquals(SUCCESS, response.getCode());
        assertEquals("가게 요청 상태 수정 완료", response.getMsg());

        verify(storeReqRepository, times(1)).save(argThat(updatedReq -> {
            assertEquals(StoreReqState.APPROVED, updatedReq.getState());
            assertEquals("testUser", updatedReq.getUpdatedBy());
            return true;
        }));

        verify(storeRepository, times(1)).save(any(P_store.class));
        verify(ratingRepository, times(1)).save(argThat(rating -> {
            assertEquals(0.0, rating.getScore());
            assertEquals("testUser", rating.getCreatedBy());
            return true;
        }));
    }

}