package com.transaction.test.controller;

import com.transaction.test.pojo.User1;
import com.transaction.test.service.User1Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class User1Controller {
    @Autowired
    private User1Service user1Service;

    @PostMapping("/getTest")
    public String getTest() {
        log.info("开始");
        User1 user1 = new User1().setName("张三");
        user1Service.addRequired(user1);

        return "结束";
    }
}
