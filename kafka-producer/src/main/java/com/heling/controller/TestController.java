package com.heling.controller;

import com.heling.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("message")
public class TestController {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private KafkaProducerService kafkaProducerService;

    //http://localhost:1111/message/send?message=消息
    @GetMapping("send")
    public boolean send(@RequestParam String message) {
        kafkaTemplate.send("myTopic", message);
        return true;
    }


    @GetMapping("send/sync")
    public boolean sendSync() {
        try {
            kafkaProducerService.sendMessageSync("myTopic", "同步消息");
        } catch (Exception e) {
            log.error("发送异常", e);
            return false;
        }
        return true;
    }

    @GetMapping("/send/async")
    public boolean sendAsync() {
        try {
            kafkaProducerService.sendMessageAsync("myTopic", "异步消息");
        } catch (Exception e) {
            log.error("发送异常", e);
            return false;
        }
        return true;
    }
}
