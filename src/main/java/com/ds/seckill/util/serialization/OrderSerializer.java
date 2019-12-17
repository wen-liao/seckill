package com.ds.seckill.util.serialization;

import com.ds.seckill.model.Order;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.util.Map;


public class OrderSerializer implements Serializer<Order> {
    @Override
    public void configure(Map configs, boolean isKey){}

    public byte[] serialize(String topic, Order order){
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.putInt(order.getProductId());
        buffer.putInt(order.getConsumerId());
        buffer.putLong(order.getTimestamp().getTime());
        buffer.putInt(order.getPaid());
        return buffer.array();
    }

    @Override
    public void close(){}
}
