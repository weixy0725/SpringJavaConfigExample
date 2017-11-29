package com.example.springsession.config;

/*import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;*/

/**
 * @description Spring Session配置类
 * @author xinyuan.wei
 * @time 2017年8月31日 下午1:09:47
 */
//@Configuration
/*
 * @EnableRedisHttpSession创建springSessionRepositoryFilter,
 * 负责替换HttpSession的实现为Spring Session支持的实现
 */
//@EnableRedisHttpSession
public class HttpSessionConfig {

	/*
	 * 创建一个RedisConnectionFactory使Spring Session连接到Redis
	 * Server，不设置IP，端口等参数时，默认连接本地6379端口
	 */
	/*@Bean
	public LettuceConnectionFactory connectionFactory() {
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
		// lettuceConnectionFactory.setHostName("127.0.0.1");
		// lettuceConnectionFactory.setPort(6379);
		return lettuceConnectionFactory;
	}
*/
	/*
	 * 在使用RESTFUL API的时候，使用Spring
	 * Session的HttpSession集成使用HTTP headers来传达当前session信息而不是使用Cookie
	 */
	/*@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		HeaderHttpSessionStrategy headerHttpSessionStrategy= new HeaderHttpSessionStrategy();
		return headerHttpSessionStrategy;
	}*/
}
