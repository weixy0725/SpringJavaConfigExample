package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.java.kafka.client.KafkaConsumerCustom;
import com.example.java.kafka.client.KafkaProducerCustom;
import com.example.java.mq.MessageQueueConsumer;
import com.example.java.mq.MessageQueueProducer;
import com.example.java.rabbitmq.client.RabbitMQConsumerCustom;
import com.example.java.rabbitmq.client.RabbitMQProducerCustom;
/**
 * @description MQ实例化配置类
 * @author xinyuan.wei
 * @time 2017年9月8日 下午2:15:26
 */
@Configuration
public class MQConfig {

	@Bean
	public MessageQueueProducer getMQProducer(){
	    return new KafkaProducerCustom();
		//return new RabbitMQProducerCustom();
		
	}
	
	@Bean(name="MQConsumer")
	public MessageQueueConsumer getMQConsumer(){
		return new KafkaConsumerCustom();
		//return new RabbitMQConsumerCustom();
	}
}
