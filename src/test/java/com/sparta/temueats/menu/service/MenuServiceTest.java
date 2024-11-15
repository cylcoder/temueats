package com.sparta.temueats.menu.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.dto.MenuCreateDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.dto.MenuUpdateDto;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.menu.repository.MenuRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static com.sparta.temueats.global.ResponseDto.SUCCESS;
import static com.sparta.temueats.menu.entity.Category.KOREAN;
import static com.sparta.temueats.store.entity.SellState.SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MenuService menuService;

    private P_store store;
    private MenuCreateDto menuCreateDto;
    private MenuUpdateDto menuUpdateDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        store = P_store.builder()
                .storeId(UUID.randomUUID())
                .name("Test Store")
                .build();

        menuCreateDto = MenuCreateDto.builder()
                .storeId(store.getStoreId())
                .name("Pizza")
                .description("Delicious pizza")
                .price(10000)
                .image("pizza.jpg")
                .category(KOREAN)
                .sellState(SALE)
                .signatureYn(true)
                .build();

        menuUpdateDto = MenuUpdateDto.builder()
                .menuId(UUID.randomUUID())
                .name("Updated Pizza")
                .description("Updated description")
                .price(12000)
                .image("updated_pizza.jpg")
                .category(KOREAN)
                .sellState(SALE)
                .signatureYn(false)
                .build();
    }

    @Test
    void testSaveMenu() {
        when(storeRepository.findById(menuCreateDto.getStoreId())).thenReturn(Optional.of(store));
        when(menuRepository.findByName(menuCreateDto.getName())).thenReturn(Optional.empty());

        P_menu savedMenu = P_menu.builder()
                .menuId(UUID.randomUUID())
                .store(store)
                .name(menuCreateDto.getName())
                .description(menuCreateDto.getDescription())
                .price(menuCreateDto.getPrice())
                .image(menuCreateDto.getImage())
                .category(menuCreateDto.getCategory())
                .sellState(menuCreateDto.getSellState())
                .signatureYn(menuCreateDto.getSignatureYn())
                .build();

        when(menuRepository.save(any(P_menu.class))).thenReturn(savedMenu);

        ResponseDto<MenuResDto> response = menuService.save(menuCreateDto);

        assertEquals(SUCCESS, response.getCode());
        verify(menuRepository, times(1)).save(any(P_menu.class));
    }

    @Test
    void testDeleteMenu() {
        UUID menuId = UUID.randomUUID();
        P_menu menu = P_menu.builder()
                .menuId(menuId)
                .store(store)
                .name("Pizza")
                .description("Delicious pizza")
                .price(10000)
                .image("pizza.jpg")
                .category(KOREAN)
                .sellState(SALE)
                .signatureYn(true)
                .build();

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        ResponseDto<Object> response = menuService.findById(menuId);

        assertEquals(response.getCode(), SUCCESS);
    }

    @Test
    void testSaveMenuWithInvalidStore() {
        when(storeRepository.findById(menuCreateDto.getStoreId())).thenReturn(Optional.empty());

        ResponseDto<MenuResDto> response = menuService.save(menuCreateDto);

        assertEquals(response.getCode(), ResponseDto.FAILURE);
        assertEquals(response.getMsg(), "유효하지 않은 가게 번호입니다.");
    }

    @Test
    void testSaveMenuWithExistingName() {
        when(storeRepository.findById(menuCreateDto.getStoreId())).thenReturn(Optional.of(store));
        when(menuRepository.findByName(menuCreateDto.getName())).thenReturn(Optional.of(new P_menu()));

        ResponseDto<MenuResDto> response = menuService.save(menuCreateDto);

        assertEquals(response.getCode(), ResponseDto.FAILURE);
        assertEquals(response.getMsg(), "이미 존재하는 메뉴입니다.");
    }

    @Test
    void testUpdateMenuWithInvalidId() {
        when(menuRepository.findById(menuUpdateDto.getMenuId())).thenReturn(Optional.empty());

        ResponseDto<MenuResDto> response = menuService.update(menuUpdateDto);

        assertEquals(response.getCode(), ResponseDto.FAILURE);
        assertEquals(response.getMsg(), "유효하지 않은 메뉴 번호입니다.");
    }

    @Test
    void testDeleteMenuWithInvalidId() {
        UUID invalidMenuId = UUID.randomUUID();
        when(menuRepository.findById(invalidMenuId)).thenReturn(Optional.empty());

        ResponseDto<Object> response = menuService.findById(invalidMenuId);

        assertEquals(response.getCode(), ResponseDto.FAILURE);
        assertEquals(response.getMsg(), "유효하지 않은 메뉴 번호입니다.");
    }

}