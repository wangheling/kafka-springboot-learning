package com.heling.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Desc：发送服务
 * @Author: heling
 * @Date: 2020/9/29 15:53
 */
@Slf4j
@Component
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * producer 同步方式发送数据
     *
     * @param topic   topic名称
     * @param message producer发送的数据
     */
    public void sendMessageSync(String topic, Object message) throws InterruptedException, ExecutionException, TimeoutException {
//        SendResult<String, String> sendResult = kafkaTemplate.send(topic, JSONObject.toJSONString(message)).get(10, TimeUnit.SECONDS);
        kafkaTemplate.send(topic, JSONObject.toJSONString(message)).get();


//        ProducerRecord<String, String> msg = new ProducerRecord<>(topic, JSONObject.toJSONString(message));
//        SendResult<String, String> sendResult = kafkaTemplate.send(msg).get();
//        log.info("同步发送消息结果：{}", JSONObject.toJSON(sendResult));
    }

    /**
     * producer 异步方式发送数据
     *
     * @param topic   topic名称
     * @param message producer发送的数据
     */
    public void sendMessageAsync(String topic, Object message) {

        kafkaTemplate.send(topic, JSONObject.toJSONString(message)).addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("send failed", throwable.getMessage());
            }

            @Override
            public void onSuccess(Object o) {
                log.info("send success:{}", JSONObject.toJSON(o));

            }
        });
    }

}
