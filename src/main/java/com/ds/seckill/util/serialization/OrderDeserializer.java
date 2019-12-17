package com.ds.seckill.util.serialization;


import com.ds.seckill.model.Order;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Map;


public class OrderDeserializer implements Deserializer<Order> {

     @Override
     public void configure(Map configs, boolean isKey) {
     }

     public Order deserialize(String topic, byte[] data) {
         Integer productId, consumerId, paid;
         Timestamp timestamp;
         if (data.length < 20)
             throw new SerializationException("Size smaller than expected.");
         ByteBuffer buffer = ByteBuffer.wrap(data);
         productId = buffer.getInt();
         consumerId = buffer.getInt();
         timestamp = new Timestamp(buffer.getLong());
         paid = buffer.getInt();
         return new Order(null, productId, consumerId, timestamp, paid);
     }

     @Override
     public void close() {
     }
 }