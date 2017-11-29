package com.example.java.zookeeper.client;

import java.util.List;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

public class GetChildrenCallback implements AsyncCallback.Children2Callback{

	@Override
	public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
		System.out.println("Get Children znode result: [response code: " + rc + ", param path: " + path + ", ctx: "
                + ctx + ", children list: " + children + ", stat: " + stat);
		
	}

}
