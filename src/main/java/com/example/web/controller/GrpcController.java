package com.example.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.java.grpc.GrpcTestAsyncClient;
import com.example.java.grpc.GrpcTestBlockingClient;
import com.example.java.grpc.GrpcTestFutureClient;
import com.example.java.grpc.HelloReply;

import io.grpc.stub.StreamObserver;

import com.example.java.grpc.GrpcConnectionManager;
@Controller
@RequestMapping(value = "/grpc")
public class GrpcController {

	private static final Logger log = LoggerFactory.getLogger(GrpcController.class);

/*	@Resource(name = "GrpcTestClient")
	private GrpcTestBlockingClient grpcTestClient;*/

/*	@Resource(name = "GrpcTestAsyncClient")
	private GrpcTestAsyncClient grpcTestAsyncClient;
*/
	@Resource(name = "GrpcTestFutureClient")
	private GrpcTestFutureClient GrpcTestFutureClient;
	
	
	@Resource(name = "GrpcConnectionManager")
	private GrpcConnectionManager<String, StreamObserver<HelloReply>> GrpcConnectionManager;
	
	@RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String GrpcTest(@RequestParam(value = "name") String name) throws Exception {
/*		grpcTestClient.greet(name);
		grpcTestClient.shutdown();
		log.info("发送成功:{}", name);*/
		return "true";
	}

	@RequestMapping(value = "/test2", method = RequestMethod.POST, produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String GrpcTestAsync(@RequestParam(value = "name") String name) throws Exception {
		/*grpcTestAsyncClient.greet(name);
		//grpcTestAsyncClient.shutdown();
		log.info("发送成功:{}", name);*/
		return "true";
	}
	
	@RequestMapping(value = "/test3", method = RequestMethod.POST, produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String GrpcTestFutrue(@RequestParam(value = "name") String name) throws Exception {
		GrpcTestFutureClient.greet(name);
		//grpcTestAsyncClient.shutdown();
		log.info("发送成功:{}", name);
		return "true";
	}
	
	@RequestMapping(value = "/test4", method = RequestMethod.POST, produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String GrpcTestResFutrue(@RequestParam(value = "name") String name) throws Exception {

		GrpcConnectionManager.send(name, "world");
		
		log.info("回复成功:{}", name);
		return "true";
	}
}
