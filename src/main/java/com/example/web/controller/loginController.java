package com.example.web.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/httpsession")
public class loginController {

	private static final Logger log = LoggerFactory.getLogger(loginController.class);

	@RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String RabbitMQTest(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, HttpSession session) {
		log.info("sessionId:{}", session.getId());
		return "true";
	}

}
