package com.example.java.grpc;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;

public class ResponseObserverManager implements GrpcConnectionManager<String, StreamObserver<HelloReply>> {

	private static final Logger log = LoggerFactory.getLogger(ResponseObserverManager.class);

	private static ConcurrentHashMap<String, StreamObserver<HelloReply>> responsePool = new ConcurrentHashMap<String, StreamObserver<HelloReply>>();

	@Override
	public void send(String key, String msg) {
		StreamObserver<HelloReply> reply = null;
		try {
	   if(!responsePool.containsKey(key)) {
			log.error("Can not find connection with identifiy of {}",key);
			return;
		}	
		reply = responsePool.get(key);
		
		HelloReply re = HelloReply.newBuilder().setMessage(key+""+msg).build();
		reply.onNext(re);
		reply.onCompleted();
		responsePool.remove(key);
		}catch(Exception e) {
			log.error("Exception:{}",e.getMessage());
			responsePool.remove(key);
		}

	}

	@Override
	public void put(String key, StreamObserver<HelloReply> con) {
		log.info("Connection with key: {} is connected.", key);
		responsePool.put(key, con);
	}

	@Override
	public void remove(String key) {
		responsePool.remove(key);
	}

}
