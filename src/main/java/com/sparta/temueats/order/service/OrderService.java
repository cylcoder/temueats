package com.sparta.temueats.order.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.coupon.service.CouponService;
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
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
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
            .id(3L)
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
    private final CouponService couponService;

    public void createDeliveryOrders(DeliveryOrderCreateRequestDto deliveryOrderCreateRequestDto, Long user) {
        // 1. 주문 생성 시 장바구니에서 선택된 물품들 가져오기
        List<P_cart> allBySelect = cartRepository.findAllBySelectAndUserId(usedUser.getId());

        if (allBySelect.isEmpty()) {
            throw new CustomApiException("장바구니에서 주문할 메뉴를 하나 이상 선택해주세요.");
        }

        // 2. 가져와서 가격 취합 후 db에 저장
        Long total = 0L;
        Long ownerId = 0L;
        for (P_cart cart : allBySelect) {
            total += cart.getMenu().getPrice().longValue();
            ownerId = cart.getMenu().getStore().getUser().getId();
        }

        // 3. 주문 객체 생성 및 저장
        P_order order = P_order.builder()
                .orderUId(UUID.randomUUID())
                .amount(total)
                .IsDelivery(true)
                .orderState(OrderState.STANDBY)
                .discountPrice(0L)
                .customerRequest(deliveryOrderCreateRequestDto.getCustomerRequest())
                .cancelYn(true)
                .cartList(allBySelect)
                .customerId(usedUser.getId())
                .ownerId(ownerId)
                .build();

        order = orderRepository.save(order);


        // 4. 쿠폰 있는 지 확인하고 사용
        List<P_coupon> coupons = couponRepository.findAllByOwnerAndStatus(usedUser, true);

        if (!coupons.isEmpty()) {
            P_coupon coupon = coupons.get(0);
            int discount = coupon.getDiscountAmount();
            Long finalTotal = total - discount;

            couponService.useCoupon(coupon.getId(), order);

            // 4-1 쿠폰 할인 금액 및 최종 금액을 적용하여 다시 저장
            order.updateDiscountPrice(discount);
            order.updateAmount(finalTotal);

        }



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
                .cancelYn(true)
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

    @Transactional
    public void cancelCustomerOrder(UUID orderId) {
        // 1. 주문 테이블에서 해당 orderId 를 찾아옴
        P_order order = orderRepository.findById(orderId).orElseThrow(() ->
                new CustomApiException("해당 주문 내역을 찾을 수 없습니다."));

        // 2. 해당 orderId의 cancelYn 체크
        if (!order.isCancelYn()) {
            throw new CustomApiException("결제 후 5분이 지나 주문을 취소할 수 없습니다.");
        }

        // 2-1. 주문 상태 변경 후 취소 정보 추가
        order.updateStatus(OrderState.FAIL);

        log.info("주문 상태 변경까지 완료");

        // 2-2. 쿠폰 적용 취소하기
        Optional<P_coupon> usedCoupon = couponRepository.findCouponByOrderId(order.getOrderId());
        couponService.cancelCoupon(usedCoupon.get().getId());

        // 3. 결제 상태를 canceled 로 설정하고 취소일시, 취소자에 정보 추가
        // todo 결제 기능 개발 후 추가

    }

    @Transactional
    public void cancelOwnerOrder(UUID orderId) {
        // 1. 주문 테이블에서 해당 orderId 를 찾아옴
        P_order order = orderRepository.findById(orderId).orElseThrow(() ->
                new CustomApiException("해당 주문 내역을 찾을 수 없습니다."));

        // 2. 해당 orderId의 cancelYn 체크
        if (!order.isCancelYn()) {
            throw new CustomApiException("결제 후 5분이 지나 주문을 취소할 수 없습니다.");
        }

        // 2-1. 주문 상태 변경 후 취소 정보 추가
        order.updateStatus(OrderState.FAIL);

        // 3. 결제 상태를 canceled 로 설정하고 취소일시, 취소자에 정보 추가
        // todo 결제 기능 개발 후 추가

    }
}
