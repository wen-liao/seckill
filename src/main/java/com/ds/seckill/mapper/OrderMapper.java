package com.ds.seckill.mapper;

import com.ds.seckill.model.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    int insertOrder(Order order);

    Order getOrder(Order order);

}
