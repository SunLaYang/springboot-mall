package com.ryan.springbootmall.controller;

import com.ryan.springbootmall.constant.ProductCategory;
import com.ryan.springbootmall.dto.ProductQueryParams;
import com.ryan.springbootmall.dto.ProductRequest;
import com.ryan.springbootmall.model.Product;
import com.ryan.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //=================查詢全部商品列表==================
    @GetMapping("/products")                        //查詢分類的商品   (required = false)代表category參數為可選
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) ProductCategory category,
                                                     @RequestParam(required = false) String search,
                                                     @RequestParam(defaultValue = "created_date") String orderBy,
                                                     @RequestParam(defaultValue = "desc") String sort){
        //修改查詢方法
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);

        List<Product> productList = productService.getProducts(productQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(productList);

    }

    //===========查看商品=======================
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);

        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
//============================新增商品===========
    @PostMapping("/products")                       //一定要加上@Valid 不然@NotNull不會生效
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
//        Integer productId = ProductService.createProduct(productRequest);
        Integer productId = productService.createProduct(productRequest);

        Product product = productService.getProductById(productId);

        return  ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    //====================修改商品===================
    @PutMapping("/products/{productId}")
    //先接住url路徑傳進來的值，接著接住要修改商品的參數
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest){

        //先檢查商品是否存在
        Product product = productService.getProductById(productId);
        //如果不存在返回404
        if(product == null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //預期survice會有一個update的方法 修改商品的數據
        productService.updateProduct(productId, productRequest);
        Product updateProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    //========================刪除商品==================
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProdect(@PathVariable Integer productId){
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
