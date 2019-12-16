package com.ds.seckill.mapper;

import com.ds.seckill.model.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    int insertProduct(Product product);

    Product[] getProductsForSale();

    int orderProduct(int id);

}
