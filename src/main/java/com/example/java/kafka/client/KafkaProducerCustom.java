package com.example.java.kafka.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.java.mq.MessageQueueProducer;

/**
 * @description kafka基于Java Api的生产者配置
 * @author xinyuan.wei
 * @time 2017年9月8日 下午2:17:46
 */
public class KafkaProducerCustom implements MessageQueueProducer {

	private static final Logger log = LoggerFactory.getLogger(KafkaProducerCustom.class);
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
		properties.put("bootstrap.servers", kafkaProperties.getProperty("kafka.producer.server"));
		properties.put("acks", kafkaProperties.getProperty("kafka.producer.ack"));
		properties.put("retries", Integer.valueOf(kafkaProperties.getProperty("kafka.producer.retries")));
		properties.put("batch.size", Integer.valueOf(kafkaProperties.getProperty("kafka.producer.batch.size")));
		properties.put("linger.ms", Integer.valueOf(kafkaProperties.getProperty("kafka.producer.linger.ms")));
		properties.put("buffer.memory", Integer.valueOf(kafkaProperties.getProperty("kafka.producer.buffer.memory")));
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return properties;
	}

	@SuppressWarnings("hiding")
	@Override
	public <String> void sendMessage(String message) {
		Producer<String, String> producer = new KafkaProducer<String, String>(producerProperties());
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(
				kafkaProperties.getProperty("kafka.topic"), message);
		producer.send(record, new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (e != null) {
					log.warn("send record error {}", e);
				}
				log.info("offset: {}, partition: {}", metadata.offset(), metadata.partition());
			}
		});
		log.info("kafka生产者发送消息为：" + message);
		producer.close();
	}

}
