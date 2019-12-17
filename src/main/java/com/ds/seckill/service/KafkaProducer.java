package com.ds.seckill.service;

import com.ds.seckill.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class KafkaProducer {

    private static final String topic = "seckill";
    private static final String key = "order";

    private Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Resource
    private KafkaTemplate<String, Order> kafkaTemplate;

    public void send(Order order){
        kafkaTemplate.send(topic, key, order);
        logger.info("Sent successfully");
    }

}
