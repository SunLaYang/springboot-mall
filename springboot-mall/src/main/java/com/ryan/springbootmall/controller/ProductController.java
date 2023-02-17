package com.ryan.springbootmall.controller;

import com.ryan.springbootmall.dto.ProductRequest;
import com.ryan.springbootmall.model.Product;
import com.ryan.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);

        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")                       //一定要加上@Valid 不然@NotNull不會生效
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
//        Integer productId = ProductService.createProduct(productRequest);
        Integer productId = productService.createProduct(productRequest);

        Product product = productService.getProductById(productId);

        return  ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
