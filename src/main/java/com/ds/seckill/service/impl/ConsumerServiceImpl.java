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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Resource
    ConsumerMapper consumerMapper;

    @Resource
    ProductMapper productMapper;

    @Resource
    OrderMapper orderMapper;

    @Override
    public DTO register(String name, String password){
        String passwordDigest = DigestUtil.digest(password);

        if(consumerMapper.getConsumerByName(name) == null){
            //TODO: transaction
            consumerMapper.insertConsumer(new Consumer(null, name, passwordDigest, null));
            return DTOUtil.newInstance(true,"000","Register Successfully",null);
        }

        return DTOUtil.newInstance(false, "101", "Consumer already exists", null);
    }

    @Override
    public DTO logIn(String name, String password, HttpSession httpSession){

        Consumer consumer = consumerMapper.getConsumerByName(name);

        //Seller doesn't exist
        if(consumer == null)
            return DTOUtil.newInstance(false, "101", "Consumer doesn't exist", null);

        //Wrong password
        if(!DigestUtil.digest(password).equals(consumer.getPasswordDigest()))
            return DTOUtil.newInstance(false, "102", "Invalid password", null);

        //Log in
        HttpSessionUtil.logIn(httpSession, name, "consumer", consumer.getId());
        return DTOUtil.newInstance(true, "000", "Logged in successfully", null);
    }

    @Override
    public DTO getProductsInformation(){
        List<Product> products = productMapper.getProductsForSale();
        Map<String, Object> data = new HashMap(){{
            put("num", products.size());
            put("products", products);
        }};
        return DTOUtil.newInstance(true, "000", "Success", data);
    }

    //TODO: Transaction

    public void beginTransaction(){}

    public void commitTranaction(){}

    public void abortTransaction(){}

    @Override
    public DTO order(int id, int consumerId){
        //TODO: transaction
        beginTransaction();
        // 1. succeed in getting a product
        if(productMapper.orderProduct(id) != 1)
            abortTransaction();
        else{
            long time = TimeUtil.getUnixEpoch();
            Order order = new Order(id, consumerId, time, 0);
            if(orderMapper.insertOrder(order) != 1)
                abortTransaction();
            else {
                // 2. create an order
                order = orderMapper.getOrder(order);
                //TODO: time-out
                return DTOUtil.newInstance(true, "000", "Order successfully", order);
            }
        }
        return DTOUtil.newInstance(false, "102", "Fail to order", null);
    }
}
