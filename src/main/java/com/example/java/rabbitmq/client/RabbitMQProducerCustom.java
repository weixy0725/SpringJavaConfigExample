package com.example.java.rabbitmq.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.java.kafka.client.KafkaProducerCustom;
import com.example.java.mq.MessageQueueProducer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @description rabbitMQ基于Java Api的生产者配置
 * @author xinyuan.wei
 * @time 2017年9月8日 下午2:17:15
 */
public class RabbitMQProducerCustom implements MessageQueueProducer {
	private static final Logger log = LoggerFactory.getLogger(RabbitMQProducerCustom.class);
	// 读取rabbitmq配置文件
	private static InputStream configFile = KafkaProducerCustom.class.getClassLoader()
			.getResourceAsStream("rabbitmq.properties");
	private static Properties rabbitmqProperties = new Properties();

	static {
		try {
			rabbitmqProperties.load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> FactoryMapProperties() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("host", rabbitmqProperties.getProperty("rabbitmq.host"));
		map.put("port", rabbitmqProperties.getProperty("rabbitmq.port"));
		return map;
	}

	@SuppressWarnings("hiding")
	@Override
	public <String> void sendMessage(String message) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setClientProperties(FactoryMapProperties());
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(rabbitmqProperties.getProperty("rabbitmq.producerQueue"), true, false, false, null);
			channel.exchangeDeclare(rabbitmqProperties.getProperty("rabbitmq.producerExchange"),
					rabbitmqProperties.getProperty("rabbitmq.exchangeType"), true);
			channel.queueBind(rabbitmqProperties.getProperty("rabbitmq.producerQueue"),
					rabbitmqProperties.getProperty("rabbitmq.producerExchange"),
					rabbitmqProperties.getProperty("rabbitmq.producerRoutingKey"));
			channel.basicPublish(rabbitmqProperties.getProperty("rabbitmq.producerExchange"),
					rabbitmqProperties.getProperty("rabbitmq.producerSendRoutingKey"), null,
					message.toString().getBytes());
			log.info("rabbitmq生产者：{}", message);
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

}
