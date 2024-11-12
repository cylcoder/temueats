package com.sparta.temueats.dummy;

import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.cart.service.CartService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class initData {

    private CartService cartService;
    private CartRepository cartRepository;

}
