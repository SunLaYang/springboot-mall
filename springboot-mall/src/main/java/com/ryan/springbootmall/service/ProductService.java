package com.ryan.springbootmall.service;

import com.ryan.springbootmall.dto.ProductRequest;
import com.ryan.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);

}
