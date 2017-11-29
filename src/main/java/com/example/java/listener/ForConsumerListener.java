package com.example.java.listener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.zookeeper.KeeperException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.java.grpc.GrpcTestAsyncServer;
import com.example.java.grpc.GrpcTestBlockingServer;
import com.example.java.grpc.GrpcTestFutureServer;
import com.example.java.mq.MessageQueueConsumer;
import com.example.java.zookeeper.client.ZookeeperCreateAsyncUsage;

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
		/*
		 * MessageQueueConsumer mqConsumer = (MessageQueueConsumer)
		 * springContext.getBean("MQConsumer"); new Thread(mqConsumer).start();
		 */
		/*
		 * ZookeeperCreateAsyncUsage create = (ZookeeperCreateAsyncUsage)
		 * springContext.getBean("ZKCreate");
		 * 
		 * try { create.create(); } catch (IOException | InterruptedException |
		 * KeeperException e) { // TODO Auto-generated catch block e.printStackTrace();
		 * }
		 */

/*		GrpcTestBlockingServer grpcTestServer = (GrpcTestBlockingServer) springContext.getBean("GrpcTestServer");

		new Thread(grpcTestServer).start();*/
		
/*		GrpcTestAsyncServer GrpcTestAsyncServer = (GrpcTestAsyncServer) springContext.getBean("GrpcTestAsyncServer");

		new Thread(GrpcTestAsyncServer).start();*/
		
		GrpcTestFutureServer GrpcTestFutureServer = (GrpcTestFutureServer) springContext.getBean("GrpcTestFutureServer");

		new Thread(GrpcTestFutureServer).start();

	}

}
