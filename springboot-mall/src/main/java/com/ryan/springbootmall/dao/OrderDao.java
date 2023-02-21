package com.ryan.springbootmall.dao;

import com.ryan.springbootmall.dto.OrderQueryPararms;
import com.ryan.springbootmall.model.Order;
import com.ryan.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer countOrder(OrderQueryPararms orderQueryPararms);

    List<Order> getOrders(OrderQueryPararms orderQueryPararms);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
