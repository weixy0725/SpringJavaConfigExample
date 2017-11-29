package com.example.java.grpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class GrpcTestAsyncClient {

	private static final Logger log = LoggerFactory.getLogger(GrpcTestAsyncClient.class);
	private final ManagedChannel channel;
	private final GreeterGrpc.GreeterStub stub;
	final CountDownLatch latch = new CountDownLatch(1);

	public GrpcTestAsyncClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port)
				// Channels are secure by default (via SSL/TLS). For the example we disable TLS
				// to avoid
				// needing certificates.
				.usePlaintext(true).build());
	}

	/**
	 * Construct client for accessing RouteGuide server using the existing channel.
	 */
	GrpcTestAsyncClient(ManagedChannel channel) {
		this.channel = channel;
		stub = GreeterGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public void greet(String name) {
		log.info("Will try to greet {} ...",name);
		HelloRequest request = HelloRequest.newBuilder().setName(name).build();
		StreamObserver<HelloReply> stream = new StreamObserver<HelloReply>() {

			@Override
			public void onNext(HelloReply value) {
				log.info("Greeting:{}", value.getMessage());
				latch.countDown();
			}

			@Override
			public void onError(Throwable t) {
				log.info("error:{}",t.getMessage());
			}

			@Override
			public void onCompleted() {
				log.info("Completed!");
				try {
					shutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		};

		try {
			stub.sayHello(request, stream);
		} catch (StatusRuntimeException e) {
			log.info("{} RPC failed:{}", Level.WARN, e.getStatus());
			return;
		}
	}
}
