package com.example.java.zookeeper.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperCreateAsyncUsage implements Watcher {

	private static final Logger log = LoggerFactory.getLogger(ZookeeperCreateAsyncUsage.class);

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	private static ZooKeeper zookeeper = null;

	public void create() throws IOException, InterruptedException, KeeperException {
		zookeeper = new ZooKeeper("10.170.7.26:2181", 5000, new ZookeeperCreateAsyncUsage());
		log.info("zookeeper的状态：{}", zookeeper.getState());

		connectedSemaphore.await();

		zookeeper.create("/zk-test-p2-/c4", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
				new CreateAsyncCallback(), "I am context. ");
		zookeeper.getChildren("/zk-test-p2-", true, new GetChildrenCallback(), null);
	}

	@Override
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
			log.info("连接已经建立！"+EventType.NodeChildrenChanged.getIntValue());
		} 
		
		if (event.getType() == EventType.NodeChildrenChanged) {
			log.info("{} 子节点变化：",event.getPath());
			try {
				zookeeper.getChildren(event.getPath(), true);
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
