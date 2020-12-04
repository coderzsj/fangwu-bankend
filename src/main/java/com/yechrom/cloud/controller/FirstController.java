package com.yechrom.cloud.controller;


import com.yechrom.cloud.dto.pojo.User;
import com.yechrom.cloud.service.UserService;
import com.yechrom.cloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@Slf4j
public class FirstController {

    @Autowired
    RedisUtil redis;

    @Autowired
    UserService user;

    @PostMapping("/first")
    public String test(){
        List<User> users = user.getUsers();
        for (User user : users) System.out.println(user.toString());
        return "ok";
    }

    @PostMapping("/info")
    public String test1(@RequestBody Map map){
        System.out.println(map);
        HashMap<String , Object> name1 = (HashMap)map.get("userinfo");
        String name = (String) name1.get("nickName");
        String action = (String) map.get("action");
        String things = (String) map.get("things");
        Long date = (Long) map.get("datetime");
        System.out.println(name + " 在" + date + "" + action + things );
        return null;
    }

    @PostMapping("/redistest")
    public String test2(){
        redis.set("123123123" , "123213123");
        log.info(redis.get("123123123"));
        return null;
    }

    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        System.out.println(uuid);
    }


}
