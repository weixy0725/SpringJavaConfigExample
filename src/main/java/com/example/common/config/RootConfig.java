package com.example.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.context.annotation.Configuration;

/**
 * @description Spring应用容器的配置文件
 * @author xinyuan.wei
 * @time 2017年8月31日 下午1:03:06
 */
@Configuration// Java 配置类
//指定bean的自动发现机制作用的范围,排除了由@EnableWebMvc注解的配置类
@ComponentScan(basePackages = { "com.example" }, excludeFilters = {
		@Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class) })
public class RootConfig {

}
