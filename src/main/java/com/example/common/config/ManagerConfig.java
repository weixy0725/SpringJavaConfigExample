package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.java.grpc.GrpcConnectionManager;
import com.example.java.grpc.HelloReply;
import com.example.java.grpc.ResponseObserverManager;

import io.grpc.stub.StreamObserver;

@Configuration
public class ManagerConfig {
	
	@Bean(name="GrpcConnectionManager")
	public GrpcConnectionManager<String, StreamObserver<HelloReply>>  getGrpcConnectionManager() {
		return new ResponseObserverManager();
	}

}
