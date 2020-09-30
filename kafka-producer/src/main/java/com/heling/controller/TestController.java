package com.heling.controller;

import com.alibaba.fastjson.JSONObject;
import com.heling.domain.User;
import com.heling.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("message")
public class TestController {

    //    @Resource
//    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private KafkaProducerService kafkaProducerService;

//    //http://localhost:1111/message/send?message=消息
//    @GetMapping("send")
//    public boolean send(@RequestParam String message) {
//        kafkaTemplate.send("myTopic", message);
//        return true;
//    }


    @GetMapping("send/sync")
    public String sendSync() {
        try {
            User user = new User();
            user.setName("heling");
            user.setAge(18);
            kafkaProducerService.sendMessageSync("myTopic", user);
        } catch (Exception e) {
            log.error("同步发送异常", e);
            return "fail";
        }
        return "success";
    }

    @GetMapping("/send/async")
    public String sendAsync() {
        try {
            User user = new User();
            user.setName("heling");
            user.setAge(18);
            kafkaProducerService.sendMessageAsync("myTopic", user);
        } catch (Exception e) {
            log.error("异步发送异常", e);
            return "fail";
        }
        return "success";
    }
}
