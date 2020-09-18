package com.heling.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Desc：
 * @Author: heling
 * @Date: 2020/9/18 10:51
 */
@Component
public class TestConsumer {

    @KafkaListener(topics = {"testTopic"})
    public void onMessage(String message){
        System.out.println(message);
    }


}
