package com.heling.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Desc：
 * @Author: heling
 * @Date: 2020/9/18 10:47
 */
@RestController
public class TestController {

    @Resource
    private KafkaTemplate<String,Object> kafkaTemplate;

    //http://localhost:6666/message/send?message=消息
    @GetMapping("/message/send")
    public boolean send(@RequestParam String message){
        kafkaTemplate.send("testTopic",message);
        return true;
    }
}