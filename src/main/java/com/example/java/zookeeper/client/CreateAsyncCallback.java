package com.example.java.zookeeper.client;

import org.apache.zookeeper.AsyncCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 异步创建zookeeper节点的回调函数
 * @author xinyuan.wei
 * @time 2017年10月23日 上午10:40:49
 */
public class CreateAsyncCallback implements AsyncCallback.StringCallback {

	private static final Logger log = LoggerFactory.getLogger(CreateAsyncCallback.class);

	@Override
	public void processResult(int rc, String path, Object ctx, String name) {
		log.info("Create path result:[{},{},{},real path name:{}", rc, path, ctx, name);
	}

}
