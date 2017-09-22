package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.java.mq.MessageQueueProducer;

@Controller
@RequestMapping(value = "/kafka")
public class kafkaController {

	@Autowired
    private MessageQueueProducer MQProducer;
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/text;charset=UTF-8")
    @ResponseBody
    public String RabbitMQTest() {
		MQProducer.sendMessage("hello world!");
        return "success";
    }
}
