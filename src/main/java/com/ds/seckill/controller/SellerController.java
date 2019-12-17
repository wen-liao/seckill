package com.ds.seckill.controller;

import com.ds.seckill.service.SellerService;
import com.ds.seckill.util.JsonUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.dto.DTOUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
@RequestMapping(
        value = {"/seller"}
)
public class SellerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    SellerService sellerService;

    @RequestMapping(
            value = {"/release"},
            method = {RequestMethod.POST},
            headers = {"Accept=application/json"},
            produces = {"application/json;charset=utf-8"}
    )
    public @ResponseBody DTO release(@RequestBody String jsonString, HttpSession httpSession){
        logger.info("/seller/release");

        //TODO: Malformed JSON body
        JsonObject data = ((JsonObject) new JsonParser().parse(jsonString)).get("data").getAsJsonObject();
        String name = JsonUtil.getFromJsonObjectAsString(data, "name"),
                description = JsonUtil.getFromJsonObjectAsString(data, "description");
        BigDecimal price = JsonUtil.getFromJsonObjectAsDouble(data, "price");
        Integer count = JsonUtil.getFromJsonObjectAsInt(data, "count");
        logger.info("name: {}", name);
        logger.info("description: {}", description);
        logger.info("price: {}", price);
        logger.info("count: {}", count);

        //not logged in
        String sellerName = (String)httpSession.getAttribute("name"),
            role = (String)httpSession.getAttribute("role");
        logger.info("sellerName: {}", sellerName);
        logger.info("role: {}", role);

        if(role == null){
            logger.info("Role name is missing.");
            return DTOUtil.newNotLoggedInDTO("100");
        }

        if(!role.equals("seller")) {
            logger.info("Role name doesn't match.");
            return DTOUtil.newMismatchedRoleDTO("101");
        }

        return sellerService.releaseProduct(sellerName, name, description, price, count);
    }
}
