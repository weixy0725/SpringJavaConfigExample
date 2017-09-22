package com.example.java.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.java.mq.MessageQueueConsumer;

/**
 * @description ServletContextListener用于启动消费者线程
 * @author xinyuan.wei
 * @time 2017年9月8日 下午2:20:04
 */
public class ForConsumerListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContext springContext = WebApplicationContextUtils
				.getWebApplicationContext(event.getServletContext());
		MessageQueueConsumer mqConsumer = (MessageQueueConsumer) springContext.getBean("MQConsumer");
		new Thread(mqConsumer).start();
	}

}
