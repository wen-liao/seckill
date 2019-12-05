package com.ds.seckill.controller;

import com.ds.seckill.service.ConsumerService;
import com.ds.seckill.util.HttpSessionUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.dto.DTOUtil;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/consumer")
public class ConsumerController {

    @Resource
    ConsumerService consumerService;

    @ResponseBody
    @RequestMapping(
            value = {"product_info"},
            method = {RequestMethod.GET},
            produces = {"application/json;charset=utf-8"}
    )
    public DTO getProductsInformation(HttpSession httpSession){

        if(!HttpSessionUtil.isConsumer(httpSession))
            return DTOUtil.newUnauthorizedAccessDTO("100");

        return consumerService.getProductsInformation();
    }

    @ResponseBody
    @RequestMapping(
            value = {"order"},
            method = {RequestMethod.POST},
            produces = {"application/json;cahrset=utf-8"}
    )
    public DTO order(@Param("id") int id, HttpSession httpSession){
        if(!HttpSessionUtil.isLoggedIn(httpSession))
            return DTOUtil.newNotLoggedInDTO("100");
        if(!HttpSessionUtil.isConsumer(httpSession))
            return DTOUtil.newMismatchedRoleDTO("101");
        int consumerId = (Integer) httpSession.getAttribute("id");
        return consumerService.order(id, consumerId);
    }
}
