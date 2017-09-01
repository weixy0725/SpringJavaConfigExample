package com.example.rabbitmq.client;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @description RabbitMQ消息发送
 * @author xinyuan.wei
 * @time 2017年8月1日 下午3:16:08
 */
public class RabbitMQProducer {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

	@Resource(name = "ProducerRabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	private String routingKey;

	public RabbitMQProducer(String routingKey) {
		this.routingKey = routingKey;
	}

	public void sendDataToQueue(Object message) {

		rabbitTemplate.convertAndSend(this.routingKey, message);

		log.info("rabbitMQ producer send message:{}", message);
	}
}
