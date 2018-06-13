package com.example.java.grpc;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class GrpcTestFutureServer implements Runnable{
	
	private static final Logger log = LoggerFactory.getLogger(GrpcTestFutureServer.class);

	private int port = 50053;

	private Server server;
	
	@Autowired
	private GrpcConnectionManager<String, StreamObserver<HelloReply>>  grpcConnectionManager;
	
	
	private void start() throws IOException {
		server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build().start();
		log.info("Server started, listening on {}", port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown
				// hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				GrpcTestFutureServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * Main launches the server from the command line.
	 */
	public void startServer() throws IOException, InterruptedException {
		start();
		blockUntilShutdown();
	}

	private class GreeterImpl extends GreeterGrpc.GreeterImplBase {
		@Override
		public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
			try {
			grpcConnectionManager.put(req.getName(), responseObserver);	
			}catch(Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void run() {
		try {
			startServer();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
