package com.ds.seckill.exception;

import io.lettuce.core.RedisException;

public class UnableToOrderException extends RedisException {
    public UnableToOrderException(String message){
        super(message);
    }
}
