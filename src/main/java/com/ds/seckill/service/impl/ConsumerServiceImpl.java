package com.ds.seckill.service.impl;

import com.ds.seckill.exception.UnableToOrderException;
import com.ds.seckill.exception.UnableToSaveOrderException;
import com.ds.seckill.mapper.ConsumerMapper;
import com.ds.seckill.mapper.OrderMapper;
import com.ds.seckill.mapper.ProductMapper;
import com.ds.seckill.model.Consumer;
import com.ds.seckill.model.Order;
import com.ds.seckill.model.Product;
import com.ds.seckill.service.ConsumerService;
import com.ds.seckill.service.KafkaProducer;
import com.ds.seckill.util.DigestUtil;
import com.ds.seckill.util.HttpSessionUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.dto.DTOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.ds.seckill.util.RedisUtil.*;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ConsumerMapper consumerMapper;

    @Resource
    ProductMapper productMapper;

    @Resource
    OrderMapper orderMapper;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    KafkaProducer kafkaProducer;

    private boolean getProduct(Integer productId){
        String product = productId.toString();
        redisTemplate.setEnableTransactionSupport(true);

        watch(redisTemplate, ProductKey, product);
        Integer count = (Integer) get(redisTemplate, ProductKey, product);
        logger.info("product: {}", product);
        logger.info("product count: {}", count);

        try{
            redisTemplate.multi();
            if(count > 0){
                increment(redisTemplate, ProductKey, product, -1);
                redisTemplate.exec();
                return true;
            }
            else {
                redisTemplate.discard();
                return false;
            }
        }catch (Exception e){
            redisTemplate.discard();
            return false;
        }
    }

    @Override
    @Transactional
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
    @Transactional
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
        Map<String, Serializable> data = new HashMap(){{
            put("num", products.length);
            put("products", products);
        }};

        DTO dto =  DTOUtil.newInstance(true, "000", "Success", data);
        logger.info("DTO: {}", dto.getData());
        return dto;
    }

    @Override
    public DTO order(int id, int consumerId) throws UnableToSaveOrderException{
        logger.info("consumerService.order({}, {})", id, consumerId);

        //TODO: Caching Strategy
        // 1. releasing the product
        // 1.1 add product to redis
        // 1.2 add product to MySQL
        // 2. ordering the product
        // 2.1 get product from redis
        // 2.2 write the order request to Kafka brokers
        // 3 Kafka consumer
        // 3.1 pull data from Kafka brokers
        // 3.2 write data to MySQL

        //TODO: transaction
        // 1. succeed in getting a product
        logger.info("productMapper.orderProduct({})",id);

        //TODO:
        // 1. acquire the product from redis
        // 2. write the requests to message queue
        // The order is considered as successful when the request is successfully written to the message queue
        if(getProduct(id)) {
            //TODO: Kafka write failures
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Order order = new Order(id, consumerId, now, 0);
            logger.info("kafkaProducer.send({})", order);
            kafkaProducer.send(order);
            return DTOUtil.newInstance(true, "000", "Order successfully", order);
        /*
        if(productMapper.orderProduct(id) != 1) {
            logger.info("Unable to make an order.");
        }
        else{
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Order order = new Order(id, consumerId, now, 0);
            logger.info("orderMapper.insertOrder({})",order);

            if(orderMapper.insertOrder(order) != 1) {
                logger.info("Unable to save an order.");
                throw new UnableToSaveOrderException();
            }
            else {
                // 2. create an order
                logger.info("orderMapper.getOrder({})", order);
                order = orderMapper.getOrder(order);
                //TODO: time-out
                return DTOUtil.newInstance(true, "000", "Order successfully", order);
            }
        }
         */
        }
        return DTOUtil.newInstance(false, "102", "Fail to order", null);
    }

    @Override
    public DTO getCartInformation(int consumerId){
        logger.info("consumerService.getCartInformation({})", consumerId);
        Order[] orders = orderMapper.getOrdersByConsumerId(consumerId);
        Map<String,Object> data = new HashMap(){{
            put("num", orders.length);
            put("orders", orders);
        }};
        return DTOUtil.newInstance(true, "000", "Success", data);
    }
}
