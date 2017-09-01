package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.rabbitmq.client.RabbitMQProducer;

@Controller
@RequestMapping(value = "/rabbitmq")
public class rabbitmqController {

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/text;charset=UTF-8")
    @ResponseBody
    public String RabbitMQTest() {
        rabbitMQProducer.sendDataToQueue("hello world!");
        return "success";
    }

}
