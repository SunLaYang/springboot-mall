package com.ryan.springbootmall.service;

import com.ryan.springbootmall.dto.CreateOrderRequset;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequset createOrderRequset);
}
