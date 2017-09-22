package com.example.java.mq;

/**
 * @description MQ消费者接口
 * @author xinyuan.wei
 * @time 2017年9月8日 下午2:16:14
 */
public interface MessageQueueConsumer extends Runnable {
	
	public void receiveMessage();
	
}
