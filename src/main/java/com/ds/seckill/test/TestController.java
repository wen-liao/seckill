package com.ds.seckill.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/test_json")
    @ResponseBody
    public Map test(){
        Person person1 = new Person(3,"Zhang"), person2 = new Person(1, "Wang");
        person2.setNext(person1);
        Object persons = new Person[] {person1, person2};

        Map<String, Object> ret = new HashMap(){{
            put("status", "100");
            put("data", "200");
        }};
        System.out.println(ret);
        return ret;
    }
}
