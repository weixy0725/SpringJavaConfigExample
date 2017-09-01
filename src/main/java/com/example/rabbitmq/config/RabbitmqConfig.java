package com.example.rabbitmq.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.example.rabbitmq.client.RabbitMQConsumer;
import com.example.rabbitmq.client.RabbitMQProducer;
import com.example.rabbitmq.util.FastJsonMessageConverter;

/**
 * @description rabbitMQ config
 * @author xinyuan.wei
 * @time 2017年8月23日 下午2:46:01
 */
@Configuration
@PropertySource(value = { "classpath:rabbitmq.properties" })
public class RabbitmqConfig {
	 
	private static final Logger log = LoggerFactory.getLogger(RabbitmqConfig.class);
	
	@Autowired
	private Environment environment;

	// 配置生产者ConnectionFactory，配置基础连接信息
	@Bean(name = "ProducerConnectionFactory")
	public ConnectionFactory ProducerConnectionFactory() throws IOException {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
				environment.getRequiredProperty("rabbitmq.host"));
		connectionFactory.setPort(Integer.parseInt(environment.getRequiredProperty("rabbitmq.port")));
		connectionFactory.setUsername(environment.getRequiredProperty("rabbitmq.username"));
		connectionFactory.setPassword(environment.getRequiredProperty("rabbitmq.password"));
		return connectionFactory;
	}

	// 配置消费者ConnectionFactory，配置基础连接信息
	@Bean(name = "ConsumerConnectionFactory")
	public ConnectionFactory ConsumerConnectionFactory() throws IOException {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
				environment.getRequiredProperty("rabbitmq.host"));
		connectionFactory.setPort(Integer.parseInt(environment.getRequiredProperty("rabbitmq.port")));
		connectionFactory.setUsername(environment.getRequiredProperty("rabbitmq.username"));
		connectionFactory.setPassword(environment.getRequiredProperty("rabbitmq.password"));
		return connectionFactory;
	}

	// 配置RabbitAdmin，在配置了多个ConnectionFactory的情况下，需要配置RabbitAdmin，否则无法自动在rabbitmq服务器中注册交换机队列等。
	@Bean
	public RabbitAdmin rabbitAdmin() throws IOException {
		return new RabbitAdmin(ConsumerConnectionFactory());
	}

	// 配置生产者Template
	@Bean(name = "ProducerRabbitTemplate")
	public RabbitTemplate ProducerRabbitTemplate() throws IOException {
		RabbitTemplate producerRabbitTemplate = new RabbitTemplate();
		producerRabbitTemplate.setConnectionFactory(ProducerConnectionFactory());
		producerRabbitTemplate.setExchange(environment.getRequiredProperty("rabbitmq.producerExchange"));
		// 配置生产者信息转换器，封装发送信息的格式
		producerRabbitTemplate.setMessageConverter(ProducerMessageConverter());
		return producerRabbitTemplate;
	}

	// 配置消费者Template
	@Bean(name = "ConsumerRabbitTemplate")
	public RabbitTemplate ConsumerRabbitTemplate() throws IOException {
		RabbitTemplate consumerRabbitTemplate = new RabbitTemplate(ConsumerConnectionFactory());
		consumerRabbitTemplate.setExchange(environment.getRequiredProperty("rabbitmq.consumerExchange"));
		return consumerRabbitTemplate;
	}

	// 配置生产者交换机类型
	@Bean(name = "ProducerExchange")
	public AbstractExchange ProducerExchange() throws IOException {
		String exchangeType = environment.getRequiredProperty("rabbitmq.exchangeType");
		if (exchangeType.contains("F")) {
			FanoutExchange fanoutExchange = new FanoutExchange(
					environment.getRequiredProperty("rabbitmq.producerExchange"), true, false);
			log.info("ProducerExchange-Fanout 生产者：广播路由");
			return fanoutExchange;
		} else if (exchangeType.contains("T")) {
			TopicExchange topicExchange = new TopicExchange(environment.getRequiredProperty("rabbitmq.producerExchange"),
					true, false);
			log.info("ProducerExchange-Topic 生产者：多路广播路由");
			return topicExchange;
		} else {
			DirectExchange directExchange = new DirectExchange(
					environment.getRequiredProperty("rabbitmq.producerExchange"), true, false);
			log.info("ProducerExchange-Direct 生产者：直接路由");
			return directExchange;
		}
	}

	// 配置消费者交换机类型
	@Bean(name = "ConsumerExchange")
	public AbstractExchange ConsumerExchange() throws IOException {
		String exchangeType = environment.getRequiredProperty("rabbitmq.exchangeType");
		if (exchangeType.contains("F")) {
			FanoutExchange fanoutExchange = new FanoutExchange(
					environment.getRequiredProperty("rabbitmq.consumerExchange"), true, false);
			log.info("ProducerExchange-Fanout 消费者：广播路由");
			return fanoutExchange;
		} else if (exchangeType.contains("T")) {
			TopicExchange topicExchange = new TopicExchange(environment.getRequiredProperty("rabbitmq.consumerExchange"),
					true, false);
			log.info("ProducerExchange-Topic 消费者：多路广播路由");
			return topicExchange;
		} else {
			DirectExchange directExchange = new DirectExchange(
					environment.getRequiredProperty("rabbitmq.consumerExchange"), true, false);
			log.info("ProducerExchange-Direct 消费者：直接路由");
			return directExchange;
		}
	}

	// 配置：生产者队列名称，重启rabbitmq服务时队列还是否存在，只被一个connection使用并且在connection关闭时queue是否被删除，当最后一个consumer取消订阅时queue是否被删除
	@Bean(name = "ProducerQueue")
	public Queue ProducerQueue() throws IOException {
		Queue producerQueue = new Queue(environment.getRequiredProperty("rabbitmq.producerQueue"), true, false, false);
		return producerQueue;
	}

	// 配置：消费者队列名称，重启rabbitmq服务时队列还是否存在，只被一个connection使用并且在connection关闭时queue是否被删除，当最后一个consumer取消订阅时queue是否被删除
	@Bean(name = "ConsumerQueue")
	public Queue ConsumerQueue() throws IOException {
		Queue consumerQueue = new Queue(environment.getRequiredProperty("rabbitmq.consumerQueue"), true, false, false);
		return consumerQueue;
	}

	// 配置生产者消息转换器
	@Bean(name = "jsonMessageConverter")
	public AbstractMessageConverter ProducerMessageConverter() {
		FastJsonMessageConverter jsonMessageConverter = new FastJsonMessageConverter();
		return jsonMessageConverter;
	}

	// 配置生产者队列与交换机之间的绑定关系和routingKey
	@Bean(name = "ProducerBinding")
	public Binding ProducerBinding() {
		Binding binding = new Binding(environment.getRequiredProperty("rabbitmq.producerQueue"), DestinationType.QUEUE,
				environment.getRequiredProperty("rabbitmq.producerExchange"),
				environment.getRequiredProperty("rabbitmq.producerRoutingKey"), null);
		return binding;
	}

	// 配置消费者队列与交换机之间的绑定关系和routingKey
	@Bean(name = "ConsumerBinding")
	public Binding ConsumerBinding() {
		Binding binding = new Binding(environment.getRequiredProperty("rabbitmq.consumerQueue"), DestinationType.QUEUE,
				environment.getRequiredProperty("rabbitmq.consumerExchange"),
				environment.getRequiredProperty("rabbitmq.consumerRoutingKey"), null);
		return binding;
	}

	// 实例化生产者代码，配置生产者要投递信息的RoutingKey
	@Bean(name = "RabbitMQProducer")
	public RabbitMQProducer RabbitMQProducer() {
		RabbitMQProducer rabbitMQProducer = new RabbitMQProducer(
				environment.getRequiredProperty("rabbitmq.producerSendRoutingKey"));
		return rabbitMQProducer;
	}

	// 实例化消费者代码
	@Bean(name = "RabbitMQConsumer")
	public RabbitMQConsumer RabbitMQConsumer() {
		RabbitMQConsumer rabbitMQConsumer = new RabbitMQConsumer();
		return rabbitMQConsumer;
	}

	// 配置监听模式，当有消息到达时会通知监听在对应的队列上的监听对象
	@Bean(name = "ListenerContainer")
	public AbstractMessageListenerContainer MessageListenerContainer() throws IOException {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(ConsumerConnectionFactory());
		simpleMessageListenerContainer.setQueueNames(environment.getRequiredProperty("rabbitmq.consumerQueue"));
		simpleMessageListenerContainer.setMessageListener(RabbitMQConsumer());
		return simpleMessageListenerContainer;
	}

}
