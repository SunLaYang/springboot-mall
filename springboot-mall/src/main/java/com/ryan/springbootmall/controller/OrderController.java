package com.ryan.springbootmall.controller;

import com.ryan.springbootmall.dto.CreateOrderRequset;
import com.ryan.springbootmall.dto.OrderQueryPararms;
import com.ryan.springbootmall.model.Order;
import com.ryan.springbootmall.service.OrderService;
import com.ryan.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        OrderQueryPararms orderQueryPararms = new OrderQueryPararms();
        orderQueryPararms.setUserId(userId);
        orderQueryPararms.setLimit(limit);
        orderQueryPararms.setOffset(offset);

        List<Order> orderList = orderService.getOrders(orderQueryPararms);
        Integer count = orderService.countOrder(orderQueryPararms);

        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);

    }

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequset createOrderRequset){

     Integer orderId = orderService.createOrder(userId, createOrderRequset);

     //同時根據訂單ID取得訂單的詳細資訊
     Order order = orderService.getOrderById(orderId);

     return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
