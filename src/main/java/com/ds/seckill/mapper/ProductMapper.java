package com.ds.seckill.mapper;

import com.ds.seckill.model.Product;
import com.ds.seckill.model.Seller;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    int insertProduct(Product product);

    List<Product> getProductsForSale();

    int orderProduct(int id);

}
