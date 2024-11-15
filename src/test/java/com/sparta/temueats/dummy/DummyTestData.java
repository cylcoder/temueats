package com.sparta.temueats.dummy;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import com.sparta.temueats.store.entity.StoreState;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DummyTestData {
    public static P_user mockCustomerUserSetting() {
        GeometryFactory geometryFactory = new GeometryFactory();
        return P_user.builder()
                .id(1L)
                .email("CustomerTest@test.com")
                .password("1234")
                .phone("010-1234-5678")
                .nickname("고객 테스트")
                .birth(Date.valueOf("2002-12-26"))
                .use_yn(true)
                .role(UserRoleEnum.CUSTOMER)
                .imageProfile("img_url")
                .latLng(geometryFactory.createPoint(new Coordinate(123, 123)))
                .address("11층 11호")
                .build();
    }

    public static P_user mockOwnerUserSetting() {
        GeometryFactory geometryFactory = new GeometryFactory();
        return P_user.builder()
                .id(2L)
                .email("OwnerTest@test.com")
                .password("1234")
                .phone("010-1234-5678")
                .nickname("주인 테스트")
                .birth(Date.valueOf("2002-12-26"))
                .use_yn(true)
                .role(UserRoleEnum.OWNER)
                .imageProfile("img_url")
                .latLng(geometryFactory.createPoint(new Coordinate(123, 123)))
                .address("222층 222호")
                .build();
    }

    public static P_store mockStoreSetting() {
        GeometryFactory geometryFactory = new GeometryFactory();
        return P_store.builder()
                .user(mockCustomerUserSetting())
                .name("얼큰가게")
                .image("img_url")
                .number("031-1111-1111")
                .state(StoreState.OPENED)
                .leastPrice(10000)
                .deliveryPrice(2000)
                .category(Category.KOREAN)
                .latLng(geometryFactory.createPoint(new Coordinate(124, 124)))
                .address("고등로 15")
                .build();
    }

    public static P_menu mockMenuSetting() {
        return P_menu.builder()
                .store(mockStoreSetting())
                .name("맛있는 김치찌개")
                .description("얼큰한 맛이 끝내주는 김치찌개입니다.")
                .price(8000)
                .image("img_url")
                .category(Category.KOREAN)
                .sellState(SellState.SALE)
                .signatureYn(true)
                .build();
    }

    public static P_cart mockCartSetting() {
        return P_cart.builder()
                .quantity(2L)
                .selectYn(false)
                .user(mockCustomerUserSetting())
                .menu(mockMenuSetting())
                .deletedYn(false)
                .build();
    }

    public static P_coupon mockCouponSetting() {
        return P_coupon.builder()
                .id(UUID.randomUUID())
                .name("테스트용 쿠폰")
                .discountAmount(2000)
                .status(true)
                .usedAt(null)
                .expiredAt(LocalDate.of(25, 5, 14))
                .owner(mockCustomerUserSetting())
                .issuer(mockCustomerUserSetting())
                .order(null)
                .build();
    }

}
