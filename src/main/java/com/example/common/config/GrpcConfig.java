package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.java.grpc.GrpcTestBlockingServer;
import com.example.java.grpc.GrpcTestBlockingClient;
import com.example.java.grpc.GrpcConnectionManager;
import com.example.java.grpc.GrpcTestAsyncClient;
import com.example.java.grpc.GrpcTestAsyncServer;
import com.example.java.grpc.GrpcTestFutureClient;
import com.example.java.grpc.GrpcTestFutureServer;
import com.example.java.grpc.HelloReply;
import com.example.java.grpc.ResponseObserverManager;

import io.grpc.stub.StreamObserver;
@Configuration
public class GrpcConfig {

/*	@Bean(name = "GrpcTestServer")
	public GrpcTestBlockingServer GrpcTestServer() {
		return new GrpcTestBlockingServer();
	}

	@Bean(name = "GrpcTestClient")
	public GrpcTestBlockingClient GrpcTestClient() {
		return new GrpcTestBlockingClient("127.0.0.1", 50051);
	}*/

/*	@Bean(name = "GrpcTestAsyncClient")
	public GrpcTestAsyncClient GrpcTestAsyncClient() {
		return new GrpcTestAsyncClient("127.0.0.1", 50052);
	}

	@Bean(name = "GrpcTestAsyncServer")
	public GrpcTestAsyncServer GrpcTestAsyncServer() {
		return new GrpcTestAsyncServer();
	}
	*/
	
	@Bean(name = "GrpcTestFutureClient")
	public GrpcTestFutureClient getGrpcTestFutureClient() {
		return new GrpcTestFutureClient("127.0.0.1", 50053);
	}

	@Bean(name = "GrpcTestFutureServer")
	public GrpcTestFutureServer getGrpcTestFutureServer() {
		return new GrpcTestFutureServer();
	}

}
