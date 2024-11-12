package com.sparta.temueats.order.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.order.dto.DeliveryOrderCreateRequestDto;
import com.sparta.temueats.order.dto.OrderGetResponseDto;
import com.sparta.temueats.order.dto.TakeOutOrderCreateRequestDto;
import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    static P_menu staticMenu = P_menu.builder()
            .menuId(UUID.randomUUID())
            .store(new P_store())
            .name("딸기 탕후루")
            .description("맛있는 딸기 탕후루입니다.")
            .price(3000)
            .image("img_url")
            .category(Category.CHINESE)
            .sellState(SellState.SALE)
            .signatureYn(true)
            .build();

    static GeometryFactory geometryFactory = new GeometryFactory();

    static P_user staticCustomerUser = P_user.builder()
            .id(4L)
            .email("email@naver.com")
            .password("임시1234")
            .phone("010-1234-5678")
            .nickname("네임")
            .birth(Date.valueOf("2002-12-26"))
            .use_yn(true)
            .role(UserRoleEnum.OWNER)
            .imageProfile("img_url")
            .latLng(geometryFactory.createPoint(new Coordinate(123, 123)))
            .address("123층 123호")
            .build();
    static P_user staticOwnerUser = P_user.builder()
            .id(5L)
            .email("email5@naver.com")
            .password("임시12345")
            .phone("010-5555-5555")
            .nickname("네임5")
            .birth(Date.valueOf("2002-12-26"))
            .use_yn(true)
            .role(UserRoleEnum.OWNER)
            .imageProfile("img_url")
            .latLng(geometryFactory.createPoint(new Coordinate(55, 55)))
            .address("555층 555호")
            .build();
    static P_user usedUser = staticCustomerUser;

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    public void createDeliveryOrders(DeliveryOrderCreateRequestDto deliveryOrderCreateRequestDto, Long user) {
        // 1. 주문 생성 시 장바구니에서 선택된 물품들 가져오기
        List<P_cart> allBySelect = cartRepository.findAllBySelectAndUserId(usedUser.getId());

        if (allBySelect.isEmpty()) {
            throw new CustomApiException("장바구니에서 주문할 메뉴를 하나 이상 선택해주세요.");
        }

        // 2. 가져와서 가격 취합 후 db에 저장
        Long total = 0L;
        for (P_cart cart : allBySelect) {
            total += cart.getMenu().getPrice().longValue();
        }

        // 3. 쿠폰 있는 지 확인하고 사용
        List<P_coupon> coupons = couponRepository.findAllByOwnerAndStatus(usedUser, true);
        int discount = coupons.get(0).getDiscountAmount();
        Long finalTotal = total - discount;

        // 4. 저장
        orderRepository.save(P_order.builder()
                        .orderUId(UUID.randomUUID())
                        .amount(finalTotal)
                        .IsDelivery(true)
                        .orderState(OrderState.STANDBY)
                        .discountPrice((long) discount)
                        .customerRequest(deliveryOrderCreateRequestDto.getCustomerRequest())
                        .cancelYn(false)
                        .cartList(allBySelect)
                        .customerId(usedUser.getId())
                        .ownerId(staticMenu.getStore().getUser().getId())
                .build());

    }

    public void createTakeOutOrders(TakeOutOrderCreateRequestDto takeOutOrderCreateRequestDto, Long user) {
        // 1. 주문 생성 시 장바구니에서 선택된 물품들 가져오기
        List<P_cart> allBySelect = cartRepository.findAllBySelectAndUserId(usedUser.getId());

        if (allBySelect.isEmpty()) {
            throw new CustomApiException("장바구니에서 주문할 메뉴를 하나 이상 선택해주세요.");
        }

        Long total = 0L;
        // 2. 주인이 금액 입력 (쿠폰 사용은 안됨)
        for (P_cart cart : allBySelect) {
            total += cart.getMenu().getPrice().longValue();
        }

        // 3. Customer 정보 저장을 위해 찾아오기
        Optional<P_user> customer = userRepository.findByEmail(takeOutOrderCreateRequestDto.getEmail());

        // 4. 저장
        orderRepository.save(P_order.builder()
                .orderUId(UUID.randomUUID())
                .amount(total)
                .IsDelivery(false)
                .orderState(OrderState.PROGRESS)
                .discountPrice(0L)
                .customerRequest(null)
                .cancelYn(false)
                .cartList(allBySelect)
                .customerId(customer.orElseThrow().getId())
                .ownerId(usedUser.getId())
                .build());
    }

    public List<OrderGetResponseDto> getCustomerOrders() {
        List<P_order> orderList = orderRepository.findAllByCustomerId(usedUser.getId());
        List<OrderGetResponseDto> responseDtoList = new ArrayList<>();

        for (P_order order : orderList) {
            responseDtoList.add(new OrderGetResponseDto(order));
        }

        return responseDtoList;
    }

    public List<OrderGetResponseDto> getOwnerOrders() {
        List<P_order> orderList = orderRepository.findAllByOwnerId(usedUser.getId());
        List<OrderGetResponseDto> responseDtoList = new ArrayList<>();

        for (P_order order : orderList) {
            responseDtoList.add(new OrderGetResponseDto(order));
        }

        return responseDtoList;
    }


    public OrderGetResponseDto getOrder(UUID orderId) {
        P_order order = orderRepository.findById(orderId).orElseThrow(() ->
                new CustomApiException("해당 주문을 찾을 수 없습니다."));
        return new OrderGetResponseDto(order);
    }

}
