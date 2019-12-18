package com.ds.seckill.service.impl;

import com.ds.seckill.mapper.ProductMapper;
import com.ds.seckill.mapper.SellerMapper;
import com.ds.seckill.model.Product;
import com.ds.seckill.model.Seller;
import com.ds.seckill.util.RedisUtil;
import com.ds.seckill.service.SellerService;
import com.ds.seckill.util.DigestUtil;
import com.ds.seckill.util.HttpSessionUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.dto.DTOUtil;
import io.lettuce.core.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static com.ds.seckill.util.RedisUtil.*;

@Service
public class SellerServiceImpl implements SellerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    SellerMapper sellerMapper;

    @Resource
    ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean putProduct(Integer productId, Integer count){
        String product = productId.toString();
        redisTemplate.setEnableTransactionSupport(true);

        //optimistic concurrency control
        logger.info("watch({}, {})", ProductKey, product);
        watch(redisTemplate, ProductKey, product);
        try {
            redisTemplate.multi();
            put(redisTemplate, ProductKey, product, count);//针对失败的情况
            redisTemplate.exec();
            return true;
        }catch (Exception e){
            redisTemplate.discard();
            return false;
        }
    }

    @Override
    public DTO register(String name, String password){
        logger.info("sellerService.register()");

        logger.info("name: {}", name);
        logger.info("password: {}", password);

        String passwordDigest = DigestUtil.digest(password);
        logger.info("passwordDigest: {}", passwordDigest);

        if(sellerMapper.getSellerByName(name) == null){
            String message = "Register Successfully";
            sellerMapper.insertSeller(new Seller(null, name, passwordDigest, null));
            logger.info("message: {}", message);
            return DTOUtil.newInstance(true,"000", message,null);
        }

        String message = "User already exists";
        logger.info("message: {}", message);
        return DTOUtil.newInstance(false, "101", message, null);
    }

    @Override
    public DTO logIn(String name, String password, HttpSession httpSession){

        Seller seller = sellerMapper.getSellerByName(name);

        //Seller doesn't exist
        if(seller == null) {
            String message = "Seller doesn't exist";
            logger.info("message: {}", message);
            return DTOUtil.newInstance(false, "101", message, null);
        }

        //Wrong password

        if(!DigestUtil.digest(password).equals(seller.getPasswordDigest()))
            return DTOUtil.newInstance(false, "102", "Invalid password", null);

        //Log in
        HttpSessionUtil.signIn(httpSession, name, "seller", seller.getId(), logger);
        return DTOUtil.newInstance(true, "000", "Logged in successfully", null);
    }

    @Override
    public DTO releaseProduct(String sellerName, String name, String description, BigDecimal price, int count){
        logger.info("sellerService.releaseProduct()");

        Seller seller = sellerMapper.getSellerByName(sellerName);
        logger.info("seller: {}", seller);

        //TODO: write product information into Redis server
        Product product = new Product(seller.getId(), name, description, count, price);
        productMapper.insertProduct(product);
        product = productMapper.getProduct(product);
        logger.info("product: {}", product);

        String message = "Release product successfully";
        logger.info(message);
        return DTOUtil.newInstance(true, "000", message, null);
    }
}
