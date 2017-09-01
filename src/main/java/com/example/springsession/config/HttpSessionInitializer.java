package com.example.springsession.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * @description 确保Servlet容器（即Tomcat）使用springSessionRepositoryFilter处理每个请求
 * @author xinyuan.wei
 * @time 2017年8月31日 下午1:09:08
 */
public class HttpSessionInitializer extends AbstractHttpSessionApplicationInitializer {

}
