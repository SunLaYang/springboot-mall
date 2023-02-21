package com.ryan.springbootmall.service;

import com.ryan.springbootmall.dao.OrderDao;
import com.ryan.springbootmall.dao.ProductDao;
import com.ryan.springbootmall.dao.UserDao;
import com.ryan.springbootmall.dto.BuyItem;
import com.ryan.springbootmall.dto.CreateOrderRequset;
import com.ryan.springbootmall.model.Order;
import com.ryan.springbootmall.model.OrderItem;
import com.ryan.springbootmall.model.Product;
import com.ryan.springbootmall.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Component
public class OrderServiceImpl implements OrderService {

    private  final  static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        //會包含訂單總資訊跟包含訂單的商品資訊
        return order;
    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequset createOrderRequset) {

        //檢查user是否存在
        User user = userDao.getUserById(userId);//先去撈數據庫userId出來

        if(user == null){
            log.warn("該 userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequset.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            //檢查商品是否存在，庫存是否足夠
            if(product == null){ //是null代表商品不存在
                log.warn("商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            }else if(product.getStock() < buyItem.getQuantity()){ //如果存在但商品庫存小於使用者購買的數量

                log.warn("商品 {} 庫存數量不足，無法購買，剩餘庫存 {} ，欲購買數量 {}",
                        buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            //扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

            //計算總價格
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            //轉換BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        //創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);


        return orderId;
    }
}
