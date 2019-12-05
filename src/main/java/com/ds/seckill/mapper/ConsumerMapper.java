package com.ds.seckill.mapper;

import com.ds.seckill.model.Consumer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsumerMapper {

    Consumer getConsumerByName(String name);

    int insertConsumer(Consumer consumer);

}
