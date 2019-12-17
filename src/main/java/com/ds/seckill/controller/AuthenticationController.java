package com.ds.seckill.controller;

import com.ds.seckill.service.ConsumerService;
import com.ds.seckill.service.SellerService;
import com.ds.seckill.util.HttpSessionUtil;
import com.ds.seckill.util.JsonUtil;
import com.ds.seckill.util.dto.DTO;
import com.ds.seckill.util.dto.DTOUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(
        value = {"/authentication"}
)
public class AuthenticationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConsumerService consumerService;

    @Autowired
    SellerService sellerService;


    @RequestMapping(
            value = {"/register"},
            method = {RequestMethod.POST},
            headers = {"Accept=application/json"},
            produces = {"application/json;charset=UTF-8"}
    )

    public @ResponseBody DTO register(@RequestBody String jsonString){

        logger.info("/authentication/register");

        JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonString);
        String name = JsonUtil.getFromJsonObjectAsString(jsonObject, "name"),
                role = JsonUtil.getFromJsonObjectAsString(jsonObject, "role"),
                password = JsonUtil.getFromJsonObjectAsString(jsonObject, "password");
        logger.info("name: " + name);
        logger.info("role: " + role);
        logger.info("password: " + password);

        if(role != null && role.equals("consumer"))
            return consumerService.register(name, password);

        if(role != null && role.equals("seller"))
            return sellerService.register(name, password);

        return DTOUtil.newInvalidUserDTO("100");
    }

    @RequestMapping(
            value = {"/sign_in"},
            method = {RequestMethod.POST}
    )
    public @ResponseBody DTO signIn(@RequestBody String jsonString, HttpSession httpSession){

        logger.info("/authentication/sign_in");

        JsonObject jsonObject = (JsonObject)new JsonParser().parse(jsonString);
        String name = JsonUtil.getFromJsonObjectAsString(jsonObject, "name"),
                role = JsonUtil.getFromJsonObjectAsString(jsonObject, "role"),
                password = JsonUtil.getFromJsonObjectAsString(jsonObject, "password");
        logger.info("name: {}", name);
        logger.info("role: {}", role);
        logger.info("password: {}", password);

        //Logged in already
        if(HttpSessionUtil.checkSignedIn(httpSession, name, role, logger)) {
            String message = "User logged in already";
            logger.info("message: {}", message);
            return DTOUtil.newInstance(true, "001", message, null);
        }
        //Not logged in
        if(role.equals("consumer"))
            return consumerService.signIn(name, password, httpSession);

        if(role.equals("seller"))
            return sellerService.logIn(name, password, httpSession);

        return DTOUtil.newInvalidUserDTO("100");
    }

    @RequestMapping(
            value = {"/sign_out"},
            method = {RequestMethod.POST}
    )
    public @ResponseBody DTO signOut(@RequestBody String jsonString, HttpSession httpSession){

        logger.info("/authentication/sign_out");
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonString);
        String username = JsonUtil.getFromJsonObjectAsString(jsonObject, "name"),
                role = JsonUtil.getFromJsonObjectAsString(jsonObject,"role");

        //not logged in
        if(!HttpSessionUtil.checkSignedIn(httpSession, username, role, logger)) {
            String message = "Not logged in.";
            logger.info("message: {}", message);
            return DTOUtil.newInstance(false, "100", message, null);
        }

        //success
        HttpSessionUtil.signOut(httpSession, logger);
        String message = "Logged out successfully.";
        return DTOUtil.newInstance(true, "000", message, null);
    }
}
