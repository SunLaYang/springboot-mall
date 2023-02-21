package com.ryan.springbootmall.rowmapper;

import com.ryan.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


//只要有取得欄位，不管從哪張表join來的 都可以用resultSet 去取得欄位的數據
public class OrderItemRowMapper implements RowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setAmount(rs.getInt("amount"));

        //可以直接新增到orderItem的class裡面，也可以創建一個新的class不去擴充原本的
        //在有join其他Table時的rowmapper寫法
        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setImageUrl(rs.getString("image_url"));

        return orderItem;
    }
}
