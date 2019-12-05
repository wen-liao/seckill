package com.ds.seckill.controller;

import com.ds.seckill.service.SellerService;
import com.ds.seckill.util.JsonUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.dto.DTOUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(
        value = {"/seller"}
)
public class SellerController {

    @Resource
    SellerService sellerService;

    @RequestMapping(
            value = {"/release"},
            method = {RequestMethod.POST},
            headers = {"Accept=application/json"},
            produces = {"application/json;charset=utf-8"}
    )
    public @ResponseBody DTO release(@RequestBody String jsonString, HttpSession httpSession){

        JsonObject data = ((JsonObject) JsonParser.parseString(jsonString)).get("username").getAsJsonObject();
        String name = JsonUtil.getFromJsonObjectAsString(data, "name"),
                description = JsonUtil.getFromJsonObjectAsString(data, "description");
        Double price = JsonUtil.getFromJsonObjectAsDouble(data, "price");
        Integer count = JsonUtil.getFromJsonObjectAsInt(data, "count");

        //not logged in
        String sellerName = (String)httpSession.getAttribute("name"),
            role = (String)httpSession.getAttribute("role");
        if(role == null)
            return DTOUtil.newNotLoggedInDTO("100");
        if(!role.equals("seller"))
            return DTOUtil.newMismatchedRoleDTO("101");

        return sellerService.releaseProduct(sellerName, name, description, price, count);
    }
}
