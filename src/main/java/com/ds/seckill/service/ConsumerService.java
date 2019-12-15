package com.ds.seckill.service;

import com.ds.seckill.util.dto.DTO;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public interface ConsumerService {

    DTO register(String name, String password);

    DTO signIn(String name, String password, HttpSession httpSession);

    DTO getProductsForSale();

    DTO order(int id, int consumerId);

    DTO getCartInformation(int id);

}
