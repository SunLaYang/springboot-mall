package com.ryan.springbootmall.dao;

import com.ryan.springbootmall.dto.ProductRequest;
import com.ryan.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);


}
