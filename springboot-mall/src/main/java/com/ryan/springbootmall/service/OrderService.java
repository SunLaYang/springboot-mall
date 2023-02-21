package com.ryan.springbootmall.service;

import com.ryan.springbootmall.dto.CreateOrderRequset;
import com.ryan.springbootmall.dto.OrderQueryPararms;
import com.ryan.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer countOrder(OrderQueryPararms orderQueryPararms);

    List<Order> getOrders(OrderQueryPararms orderQueryPararms);

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequset createOrderRequset);
}
