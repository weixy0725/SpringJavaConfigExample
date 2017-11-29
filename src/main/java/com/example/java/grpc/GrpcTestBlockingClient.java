package com.example.java.grpc;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcTestBlockingClient {

	private static final Logger log = LoggerFactory.getLogger(GrpcTestBlockingClient.class);

	private final ManagedChannel channel;
	private final GreeterGrpc.GreeterBlockingStub blockingStub;

	/** Construct client connecting to HelloWorld server at {@code host:port}. */
	  public GrpcTestBlockingClient(String host, int port) {
	    this(ManagedChannelBuilder.forAddress(host, port)
	        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
	        // needing certificates.
	        .usePlaintext(true)
	        .build());
	  }

	/** Construct client for accessing RouteGuide server using the existing channel. */
	  GrpcTestBlockingClient(ManagedChannel channel) {
	    this.channel = channel;
	    blockingStub = GreeterGrpc.newBlockingStub(channel);
	  }

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	/** Say hello to server. */
	public void greet(String name) {
		log.info("Will try to greet " + name + " ...");
		HelloRequest request = HelloRequest.newBuilder().setName(name).build();
		HelloReply response;
		try {
			response = blockingStub.sayHello(request);
		} catch (StatusRuntimeException e) {
			log.info("{} RPC failed:{}",Level.WARN,e.getStatus());
			return;
		}
		log.info("Greeting: " + response.getMessage());
	}

}
