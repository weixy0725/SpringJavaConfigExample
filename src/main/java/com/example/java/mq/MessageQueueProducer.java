package com.example.java.mq;

/**
 * @description MQ生产者接口
 * @author xinyuan.wei
 * @time 2017年9月8日 下午2:15:52
 */
public interface MessageQueueProducer {

	public <T> void sendMessage(T message);

}
