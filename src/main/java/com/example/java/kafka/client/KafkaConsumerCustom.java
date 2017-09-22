package com.example.java.kafka.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.java.mq.MessageQueueConsumer;

/**
 * @description kafka基于Java Api的消费者配置
 * @author xinyuan.wei
 * @time 2017年9月8日 下午2:17:46
 */
public class KafkaConsumerCustom implements MessageQueueConsumer {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerCustom.class);

	// 读取kafka配置文件
	private static InputStream configFile = KafkaProducerCustom.class.getClassLoader()
			.getResourceAsStream("kafka.properties");
	private static Properties kafkaProperties = new Properties();

	static {
		try {
			kafkaProperties.load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Properties producerProperties() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", kafkaProperties.getProperty("kafka.consumer.server"));
		properties.put("group.id", kafkaProperties.getProperty("kafka.consumer.group.id"));
		properties.put("enable.auto.commit",
				Boolean.valueOf(kafkaProperties.getProperty("kafka.consumer.enable.auto.commit")));
		properties.put("auto.commit.interval.ms",
				Integer.valueOf(kafkaProperties.getProperty("kafka.consumer.auto.commit.interval.ms")));
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return properties;
	}

	// 自动提交偏移量
	public void receiveMessage() {

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(producerProperties());
		consumer.subscribe(Arrays.asList(kafkaProperties.getProperty("kafka.topic")));
		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(100);
				for (ConsumerRecord<String, String> record : records) {
					log.info("kafka消费的者收到信息：partition = {}, offset = {}, key = {}, value = {}", record.partition(),
							record.offset(), record.key(), record.value());
				}
			}
		} finally {
			consumer.close();
		}
	}

	@Override
	public void run() {
		receiveMessage();
	}

}
