package com.example.java.grpc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcTestFutureClient {

	private static final Logger log = LoggerFactory.getLogger(GrpcTestFutureClient.class);
	private final ManagedChannel channel;
	private final GreeterGrpc.GreeterFutureStub futureStub;

	public GrpcTestFutureClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port)
				// Channels are secure by default (via SSL/TLS). For the example we disable TLS
				// to avoid
				// needing certificates.
				.usePlaintext(true).build());
	}

	/**
	 * Construct client for accessing RouteGuide server using the existing channel.
	 */
	GrpcTestFutureClient(ManagedChannel channel) {
		this.channel = channel;
		futureStub = GreeterGrpc.newFutureStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public void greet(String name) throws InterruptedException, ExecutionException {
		log.info("Will try to greet " + name + " ...");
		HelloRequest request = HelloRequest.newBuilder().setName(name).build();
		try {
			ListenableFuture<HelloReply> listener = futureStub.sayHello(request);
			//listener.addListener(listener, executor);
			HelloReply response = listener.get();
			log.info("response:{}",response.getMessage());
			
		} catch (StatusRuntimeException e) {
			log.info("{0} RPC failed:{1}", Level.WARN, e.getStatus());
			return;
		}
	}
}
