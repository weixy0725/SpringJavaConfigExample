package com.example.java.grpc;

import javax.annotation.Resource;

import io.grpc.stub.StreamObserver;

public class runTest implements Runnable{
	
	private HelloRequest hr = null;
	private StreamObserver<HelloReply> reply = null;
	
	@Resource(name = "GrpcConnectionManager")
	private GrpcConnectionManager<String, Object> grpcConnectionManager;

	public runTest(HelloRequest hello,StreamObserver<HelloReply> res) {
		hr = hello;
		reply = res;
	}

	@Override
	public void run() {
		grpcConnectionManager.put(hr.getName(), reply);		
	}

}
