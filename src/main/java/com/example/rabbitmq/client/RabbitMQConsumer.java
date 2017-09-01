package com.example.rabbitmq.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @description RabbitMQ消息接收
 * @author xinyuan.wei
 * @time 2017年8月23日 下午3:38:03
 */
public class RabbitMQConsumer implements MessageListener {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

	public void onMessage(Message message) {
		String data = new String(message.getBody());
		log.info("rabbitMQ consumer receive message：{}",data);
	}
}
