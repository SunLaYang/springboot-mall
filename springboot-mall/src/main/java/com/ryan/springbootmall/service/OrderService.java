package com.ryan.springbootmall.service;

import com.ryan.springbootmall.dto.CreateOrderRequset;
import com.ryan.springbootmall.model.Order;

public interface OrderService {

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequset createOrderRequset);
}
