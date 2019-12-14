package com.ds.seckill.service.impl;

import com.ds.seckill.mapper.ConsumerMapper;
import com.ds.seckill.mapper.OrderMapper;
import com.ds.seckill.mapper.ProductMapper;
import com.ds.seckill.model.Consumer;
import com.ds.seckill.model.Order;
import com.ds.seckill.model.Product;
import com.ds.seckill.service.ConsumerService;
import com.ds.seckill.util.DigestUtil;
import com.ds.seckill.util.HttpSessionUtil;
import com.ds.seckill.util.TimeUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.dto.DTOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ConsumerMapper consumerMapper;

    @Resource
    ProductMapper productMapper;

    @Resource
    OrderMapper orderMapper;

    @Override
    public DTO register(String name, String password){
        logger.info("consumerService.register()");
        logger.info("name: {}", name);
        logger.info("password: {}", password);

        String passwordDigest = DigestUtil.digest(password);
        logger.info("password digest: {}", passwordDigest);

        if(consumerMapper.getConsumerByName(name) == null){
            //TODO: transaction
            String message = "Register Successfully";
            logger.info("message: {}", message);
            consumerMapper.insertConsumer(new Consumer(null, name, passwordDigest, null));
            return DTOUtil.newInstance(true,"000",message,null);
        }

        String message = "Consumer already exists";
        logger.info("message: {}", message);
        return DTOUtil.newInstance(false, "101", "Consumer already exists", null);
    }

    @Override
    public DTO signIn(String name, String password, HttpSession httpSession){

        Consumer consumer = consumerMapper.getConsumerByName(name);

        logger.info("consumer: " + consumer.toString());

        //Consumer doesn't exist
        if(consumer == null) {
            String message = "Consumer doesn't exist";
            logger.info("message: {}", message);
            return DTOUtil.newInstance(false, "101", message, null);
        }

        String passwordDigest = consumer.getPasswordDigest();
        String inputPasswordDigest = DigestUtil.digest(password);
        logger.info("password digest: {}", passwordDigest);
        logger.info("input password digests: {}", inputPasswordDigest);
        //Wrong password
        if(!passwordDigest.equals(inputPasswordDigest)) {
            String message = "Invalid password";
            logger.info("message: {}", message);
            return DTOUtil.newInstance(false, "102", message, "Hello");
        }

        //Log in
        HttpSessionUtil.signIn(httpSession, name, "consumer", consumer.getId(), logger);
        String message = "Logged in successfully";
        logger.info("message: {}", message);
        return DTOUtil.newInstance(true, "000", message, null);
    }

    @Override
    public DTO getProductsForSale(){
        logger.info("consumerService.getProductsForSale()");

        Product[] products = productMapper.getProductsForSale();
        logger.info("products: {}", products);
        HashMap<String, Serializable> data = new HashMap(){{
            put("num", products.length);
            put("products", products);
        }};

        DTO dto =  DTOUtil.newInstance(true, "000", "Success", data);
        logger.info("DTO: {}", dto.getData());
        return dto;
    }

    @Override
    public DTO order(int id, int consumerId){
        logger.info("consumerService.order(id, consumerId)");
        logger.info("id: {}",id);
        logger.info("consumerId: {}", consumerId);

        //TODO: transaction
        // 1. succeed in getting a product
        logger.info("productMapper.orderProduct({})",id);
        if(productMapper.orderProduct(id) != 1) {
            logger.info("Unable to make an order.");
            //throw new SQLException("unable to get a product");
        }
        else{
            Date now = new Date();
            Order order = new Order(id, consumerId, now, 0);
            logger.info("orderMapper.insertOrder({})",order);
            if(orderMapper.insertOrder(order) != 1) {
                logger.info("Unable to save an order.");
                //throw new SQLException("unable to create an order");
            }
            else {
                // 2. create an order
                logger.info("orderMapper.getOrder({})", order);
                order = orderMapper.getOrder(order);
                //TODO: time-out
                return DTOUtil.newInstance(true, "000", "Order successfully", order);
            }
        }
        return DTOUtil.newInstance(false, "102", "Fail to order", null);
    }
}
