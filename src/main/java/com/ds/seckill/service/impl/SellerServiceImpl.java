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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    SellerMapper sellerMapper;

    @Resource
    ProductMapper productMapper;

    @Override
    public DTO register(String name, String password){
        String passwordDigest = DigestUtil.digest(password);

        if(sellerMapper.getSellerByName(name) == null){
            //TODO: transaction
            sellerMapper.insertSeller(new Seller(null, name, passwordDigest, null));
            return DTOUtil.newInstance(true,"000","Register Successfully",null);
        }

        return DTOUtil.newInstance(false, "101", "User already exists", null);
    }

    @Override
    public DTO logIn(String name, String password, HttpSession httpSession){

        Seller seller = sellerMapper.getSellerByName(name);

        //Seller doesn't exist
        if(seller == null)
            return DTOUtil.newInstance(false, "101", "Seller doesn't exist", null);

        //Wrong password
        if(!DigestUtil.digest(password).equals(seller.getPasswordDigest()))
            return DTOUtil.newInstance(false, "102", "Invalid password", null);

        //Log in
        HttpSessionUtil.logIn(httpSession, name, "seller", seller.getId());
        return DTOUtil.newInstance(true, "000", "Logged in successfully", null);
    }

    @Override
    public DTO releaseProduct(String sellerName, String name, String description, double price, int count){
        Seller seller = sellerMapper.getSellerByName(sellerName);
        productMapper.insertProduct(new Product(seller, name, description, price, count));
        return DTOUtil.newInstance(true, "000", "Release product successfully", null);
    }
}
