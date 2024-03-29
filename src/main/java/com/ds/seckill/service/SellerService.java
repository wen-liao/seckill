package com.ds.seckill.service;

import com.ds.seckill.util.dto.DTO;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

public interface SellerService {

    DTO register(String name, String password);

    DTO logIn(String name, String password, HttpSession httpSession);

    //TODO: price
    DTO releaseProduct(String sellerName, String name, String description, BigDecimal price, int count);
}
