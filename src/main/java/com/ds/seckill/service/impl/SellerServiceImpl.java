package com.ds.seckill.service.impl;

import com.ds.seckill.mapper.ProductMapper;
import com.ds.seckill.mapper.SellerMapper;
import com.ds.seckill.model.Product;
import com.ds.seckill.model.Seller;
import com.ds.seckill.service.SellerService;
import com.ds.seckill.util.HttpSessionUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.DigestUtil;
import com.ds.seckill.util.dto.DTOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Service
public class SellerServiceImpl implements SellerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    SellerMapper sellerMapper;

    @Resource
    ProductMapper productMapper;

    @Override
    public DTO register(String name, String password){
        logger.info("sellerService.register()");

        logger.info("name: {}", name);
        logger.info("password: {}", password);

        String passwordDigest = DigestUtil.digest(password);
        logger.info("passwordDigest: {}", passwordDigest);

        if(sellerMapper.getSellerByName(name) == null){
            //TODO: transaction
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
        productMapper.insertProduct(new Product(seller.getId(), name, description, count, price));

        String message = "Release product successfully";
        logger.info(message);
        return DTOUtil.newInstance(true, "000", message, null);
    }
}
