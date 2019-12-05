package com.ds.seckill.util;

import org.springframework.util.DigestUtils;

public class DigestUtil {

    public static String digest(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
