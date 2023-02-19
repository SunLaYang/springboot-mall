package com.ryan.springbootmall.controller;

import com.ryan.springbootmall.constant.ProductCategory;
import com.ryan.springbootmall.dto.ProductQueryParams;
import com.ryan.springbootmall.dto.ProductRequest;
import com.ryan.springbootmall.model.Product;
import com.ryan.springbootmall.service.ProductService;
import com.ryan.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //=================查詢全部商品列表==================
    @GetMapping("/products")                        //查詢分類的商品   (required = false)代表category參數為可選
    public ResponseEntity<Page<Product>> getProducts(@RequestParam(required = false) ProductCategory category,
                                            @RequestParam(required = false) String search,
                                            @RequestParam(defaultValue = "created_date") String orderBy,
                                            @RequestParam(defaultValue = "desc") String sort,
                                            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
                                            @RequestParam(defaultValue = "0") @Min(0) Integer offset){
        //修改查詢方法
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        //取得productList 表示取得商品表的數據
        List<Product> productList = productService.getProducts(productQueryParams);


        //根據傳進去的參數去確認商品總數有多少筆
        Integer total = productService.countProduct(productQueryParams);

        //分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);

        return ResponseEntity.status(HttpStatus.OK).body(page);

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
