package com.ryan.springbootmall.dao;

import com.ryan.springbootmall.constant.ProductCategory;
import com.ryan.springbootmall.dto.ProductRequest;
import com.ryan.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts(ProductCategory category, String search);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);


}
