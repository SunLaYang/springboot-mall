package com.ryan.springbootmall.dao;

import com.ryan.springbootmall.dto.ProductQueryParams;
import com.ryan.springbootmall.dto.ProductRequest;
import com.ryan.springbootmall.model.Product;
import com.ryan.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductDaoImpl implements  ProductDao{

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;



    //==================

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(sql, map, productQueryParams);

//        if(productQueryParams.getCategory() != null){
//            sql = sql + " AND category = :category";
//            map.put("category", productQueryParams.getCategory().name());//使用enum要調用name方法 才能把enum轉成字串加進去
//
//        }
//
//        if(productQueryParams.getSearch() != null) {
//            sql = sql + " AND product_name LIKE :search";
//            map.put("search", "%" +productQueryParams.getSearch() + "%");
//        }

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);//表示將count的值轉成Integer的值

        return total;
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date From product WHERE 1=1";
        //WHERE 1 = 1 就跟廢話一樣對查詢結果沒引響，主要是為了讓下面的查詢條件能自由拼接 WHERE 1 = 1 AND category = :category
        // !!!!!!!!!!一定要在AND前面留一個空白鍵!!!!!!!!!!!!!!!!!

        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(sql, map, productQueryParams);

//        if(productQueryParams.getCategory() != null){
//            sql = sql + " AND category = :category";
//            map.put("category", productQueryParams.getCategory().name());//使用enum要調用name方法 才能把enum轉成字串加進去
//
//        }
//
//        if(productQueryParams.getSearch() != null) {
//            sql = sql + " AND product_name LIKE :search";
//            map.put("search", "%" +productQueryParams.getSearch() + "%");
//        }

        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        sql= sql + " LIMIT :limit OFFSET :offset";

        map.put("limit",productQueryParams.getLimit());
        map.put("offset",productQueryParams.getOffset());


        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        return productList;
    }

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date From product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (productList.size() > 0) {
            return productList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
//        INSERT INTO `mall`.`product` (`product_name`, `category`, `image_url`, `price`, `stock`, `description`, `created_date`, `last_modified_date`) VALUES ('test', 'CAR', 'https://hips.hearstapps.com/hmg-prod/images/2023-mclaren-artura-101-1655218102.jpg?crop=1.00xw:0.847xh;0,0.153xh&resize=1200:*', '1000', '5', '到底搞啥', '2022-03-01 02:41:28', '2022-03-01 02:41:32');

        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, description, created_date, last_modified_date) VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        //接收前端請求參數
        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        //創造一個Date 去創造當前時間並put到map裡
        Date  now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        //使用keyholder去儲存資料庫自動生成的productId
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int productId = keyHolder.getKey().intValue();

        //最後回傳出去儲存的productId
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :productName, category = :category, image_url = :imageUrl, price = :price, stock = :stock, description = :description, last_modified_date = :lastModifiedDate  WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate", new Date());
        namedParameterJdbcTemplate.update(sql, map);

    }

    //更新庫存
    @Override
    public void updateStock(Integer productId, Integer stock) {
        //把庫存更新到指定的值，也順便更新最後修改的時間，因為有修改庫存的動作，最後修改時間也應該要更動
        String sql = "UPDATE product SET stock = :stock, last_modified_date = :lastModifiedDate  WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("stock", stock);
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);

    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);

    }

    //把上面重複的sql語句提煉出來
    private String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParams productQueryParams){

        if(productQueryParams.getCategory() != null){
            sql = sql + " AND category = :category";
            map.put("category", productQueryParams.getCategory().name());//使用enum要調用name方法 才能把enum轉成字串加進去

        }

        if(productQueryParams.getSearch() != null) {
            sql = sql + " AND product_name LIKE :search";
            map.put("search", "%" +productQueryParams.getSearch() + "%");
        }

        return sql;

    }
}
