package com.heling.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Desc：
 * @Author: heling
 * @Date: 2020/9/18 10:51
 */
@Slf4j
@Component
public class TestConsumer {

    @KafkaListener(
            topics = {"myTopic"},
            groupId = "defaultGroup",
            containerFactory = "kafkaListenerContainerFactory"// * 使用的 KafkaListenerContainerFactory Bean 的名字
                                                              // * 若未设置，则使用默认的 KafkaListenerContainerFactory Bean
    )
    public void onMessage(String message) {
        log.info("received message: {}", message);

    }

    /**
     * @desc: 需要手动确认 如果设置 AckMode 模式为 MANUAL 或者 MANUAL_IMMEDIATE，则需要对监听消息的方法中，
     * 引入 Acknowledgment 对象参数，并调用 acknowledge() 方法进行手动提交
     * @param:
     * @return:
     * @author: heling
     */
//    @KafkaListener(topics = {"myTopic"}, groupId = "defaultGroup", containerFactory = "kafkaListenerContainerFactory")
//    public void onMessage(String message,
//                          Acknowledgment acknowledgment) {
//        log.info("received message: {}", message);
//        //原文地址：https://stackoverflow.com/questions/60559265/how-to-not-commit-offsets-in-kafkalistener
//    /*Starting with version 2.3, the Acknowledgment interface has two additional methods nack(long sleep) and nack(int index, long sleep).
//    The first one is used with a record listener, the second with a batch listener. Calling the wrong method for your listener type will
//    throw an IllegalStateException.*/
////        acknowledgment.nack(long sleep);  //a record listener 如果是a batch listener，那么使用该api会报错 Exception: nack(index, sleep) is not supported by this Acknowledgement.
////        acknowledgment.nack(int index, long sleep); //a batch listener
//        acknowledgment.acknowledge();
//    }

}
