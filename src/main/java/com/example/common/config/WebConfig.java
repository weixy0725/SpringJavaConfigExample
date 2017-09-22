package com.example.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @description Spring MVC应用容器的配置文件
 * @author xinyuan.wei
 * @time 2017年8月31日 上午10:38:56
 */
@Configuration // Java 配置类
@EnableWebMvc // 启动Spring MVC特性
// 指定bean的自动发现机制作用的范围,被@Controller等注解修饰的web的bean将被发现并加载到spring mvc应用容器
@ComponentScan("com.example.web")
public class WebConfig extends WebMvcConfigurerAdapter {

	/*
	 * //配置JSP视图解析器
	 * 
	 * @Bean public ViewResolver viewResolver() { InternalResourceViewResolver
	 * resolver = new InternalResourceViewResolver();
	 * resolver.setPrefix("/WEB-INF/views/"); resolver.setSuffix(".jsp");
	 * resolver.setExposeContextBeansAsAttributes(true);//可以在JSP页面中通过${}访问beans
	 * return resolver; }
	 */

	// 配置静态文件处理
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}
