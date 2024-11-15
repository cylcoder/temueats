package com.sparta.temueats.order.service;

import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderOwnerServiceTest {

    @InjectMocks
    private OrderOwnerService orderOwnerService;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderRepository orderRepository;


    @Test
    void createTakeOutOrders() {
    }
}