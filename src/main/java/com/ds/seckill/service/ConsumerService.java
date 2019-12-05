package com.ds.seckill.service;

import com.ds.seckill.util.dto.DTO;

import javax.servlet.http.HttpSession;

public interface ConsumerService {

    DTO register(String name, String password);

    DTO logIn(String name, String password, HttpSession httpSession);

    DTO getProductsInformation();

    DTO order(int id, int consumerId);

}
