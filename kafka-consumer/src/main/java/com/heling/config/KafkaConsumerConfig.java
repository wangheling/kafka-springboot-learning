package com.heling.config;

import com.heling.constant.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/*
    创建 Consumer 配置类，对 Kafka 消费者进行配置,在配置中需要设置三个 Bean 分别为：
        kafkaListenerContainerFactory：kafka container 工厂，负责创 建container，当使用@KafkaListener时需要提供。
        consumerFactory：consumer 工厂，用于对 kafka consumer 进行配置。
        consumerConfigs：对 kafka consumer 参数进行配置。
*/
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        Spring For Kafka 提供了消息监听器接口的两种实现类，分别是：
//        1.KafkaMessageListenerContainer       利用单个线程来接收全部主题中全部分区上的所有消息;
//        2.ConcurrentMessageListenerContainer  代理的一个或多个 KafkaMessageListenerContainer 实例，来实现多个线程消费;
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 设置消费者工厂
        factory.setConsumerFactory(consumerFactory());
        // 消费者组中线程数量
        factory.setConcurrency(3);
        // 拉取超时时间
        factory.getContainerProperties().setPollTimeout(3000);
        // 当使用批量监听器时需要设置为true
        factory.setBatchListener(true);
        return factory;
    }

    //手动提交
//    @Bean
//    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        // 设置消费者工厂
//        factory.setConsumerFactory(consumerFactory());
//        // 消费者组中线程数量
//        factory.setConcurrency(3);
//        // 拉取超时时间
//        factory.getContainerProperties().setPollTimeout(3000);
//        // 当使用批量监听器时需要设置为true
//        factory.setBatchListener(false);
//        /*auto.commit.enable=false
//        在 kafkaListenerContainerFactory 配置中设置 AckMode，它有七种模式分别为：
//
//          RECORD： 每处理完一条记录后提交。
//          BATCH(默认)： 每次poll一批数据后提交一次，频率取决于每次poll的调用频率。
//          TIME： 每次间隔ackTime的时间提交。
//          COUNT： 处理完poll的一批数据后并且距离上次提交处理的记录数超过了设置的ackCount就提交。
//          COUNT_TIME： TIME和COUNT中任意一条满足即提交。
//          MANUAL： 手动调用Acknowledgment.acknowledge()后，并且处理完poll的这批数据后提交。
//          MANUAL_IMMEDIATE： 手动调用Acknowledgment.acknowledge()后立即提交。
//
//          注意：如果设置 AckMode 模式为 MANUAL 或者 MANUAL_IMMEDIATE，则需要对监听消息的方法中，引入 Acknowledgment 对象参数，
//          并调用 acknowledge() 方法进行手动提交*/
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        // Kafka地址
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.bootstrapServers);
        //配置默认分组，这里没有配置+在监听的地方没有设置groupId，多个服务会出现收到相同消息情况
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "defaultGroup");
        // 是否自动提交offset偏移量(默认true)
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 自动提交的频率(ms) 当自动提交=true才生效
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        // Session超时设置
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        // 键的反序列化方式
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 值的反序列化方式
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // offset偏移量规则设置：
        // (1)、earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        // (2)、latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        // (3)、none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return propsMap;
    }

}
