package com.ds.seckill.controller;

import com.ds.seckill.service.ConsumerService;
import com.ds.seckill.util.HttpSessionUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.dto.DTOUtil;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/consumer")
public class ConsumerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ConsumerService consumerService;

    @ResponseBody
    @RequestMapping(
            value = {"/product_info"},
            method = {RequestMethod.GET},
            produces = {"application/json;charset=utf-8"}
    )
    public DTO getProductsInformation(HttpSession httpSession){
        logger.info("/consumer/product_info");

        if(!HttpSessionUtil.isConsumer(httpSession, logger))
            return DTOUtil.newUnauthorizedAccessDTO("100");

        return consumerService.getProductsForSale();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/order"},
            method = {RequestMethod.POST},
            produces = {"application/json;charset=utf-8"}
    )
    public DTO order(@Param("id") int id, HttpSession httpSession) throws Exception{
        logger.info("/consumer/order");
        logger.info("id: {}", id);

        if(!HttpSessionUtil.isSignedIn(httpSession, logger))
            return DTOUtil.newNotLoggedInDTO("100");
        if(!HttpSessionUtil.isConsumer(httpSession, logger))
            return DTOUtil.newMismatchedRoleDTO("101");
        int consumerId = (Integer) httpSession.getAttribute("id");
        return consumerService.order(id, consumerId);
    }

    @ResponseBody
    @RequestMapping(
            value = {"/cart_info"},
            method = {RequestMethod.GET},
            produces = {"application/json;charset=utf-8"}
    )
    public DTO getCartInformation(HttpSession httpSession){
        logger.info("/cart_info");
        if(!HttpSessionUtil.isSignedIn(httpSession, logger))
            return DTOUtil.newNotLoggedInDTO("100");
        if(!HttpSessionUtil.isConsumer(httpSession, logger))
            return DTOUtil.newMismatchedRoleDTO("101");
        //TODO: getCartInfo
        int consumerId = (Integer) httpSession.getAttribute("id");
        return consumerService.getCartInformation(consumerId);
    }
}
