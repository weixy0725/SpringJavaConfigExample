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
import com.example.java.mq.MessageQueueConsumer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @description rabbitMQ基于Java Api的消费者配置
 * @author xinyuan.wei
 * @time 2017年9月8日 下午2:16:40
 */
public class RabbitMQConsumerCustom implements MessageQueueConsumer {

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

	private Map<String, Object> factoryMapProperties() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("host", rabbitmqProperties.getProperty("rabbitmq.host"));
		map.put("port", rabbitmqProperties.getProperty("rabbitmq.port"));
		return map;
	}

	@Override
	public void receiveMessage() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setClientProperties(factoryMapProperties());
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(rabbitmqProperties.getProperty("rabbitmq.consumerQueue"), true, false, false, null);
			channel.exchangeDeclare(rabbitmqProperties.getProperty("rabbitmq.consumerExchange"),
					rabbitmqProperties.getProperty("rabbitmq.exchangeType"), true);
			channel.queueBind(rabbitmqProperties.getProperty("rabbitmq.consumerQueue"),
					rabbitmqProperties.getProperty("rabbitmq.consumerExchange"),
					rabbitmqProperties.getProperty("rabbitmq.consumerRoutingKey"));
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					log.info("rabbitmq消费者收到消息：{}", message);
				}
			};
			channel.basicConsume(rabbitmqProperties.getProperty("rabbitmq.consumerQueue"), true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		receiveMessage();
	}
}
