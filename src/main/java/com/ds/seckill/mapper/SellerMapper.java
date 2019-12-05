package com.ds.seckill.mapper;

import com.ds.seckill.model.Seller;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SellerMapper {

    Seller getSellerByName(String name);

    int insertSeller(Seller seller);

}
