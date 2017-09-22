package com.example.common.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.example.java.listener.ForConsumerListener;
import com.example.springsession.config.HttpSessionConfig;

/**
 * @description 配置DispatcherServlet、初始化Spring MVC容器和Spring容器
 * @author xinyuan.wei
 * @time 2017年8月31日 上午10:07:28
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext rootAppContext = createRootApplicationContext();
		if (rootAppContext != null) {
			ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
			listener.setContextInitializers(getRootApplicationContextInitializers());
			servletContext.addListener(listener);
			//配置ServletContextListener用于启动消费者线程
			servletContext.addListener(new ForConsumerListener());
		} else {
			logger.debug("No ContextLoaderListener registered, as "
					+ "createRootApplicationContext() did not return an application context");
		}
		registerDispatcherServlet(servletContext);
	}

	/*
	 * 获取Spring应用容器的配置文件: RootConfig.class为基础的Spring容器配置文件；
	 * HttpSessionConfig.class为Spring Session 配置文件;
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class, HttpSessionConfig.class };
	}

	// 获取Spring MVC应用容器的配置文件:WebConfig.class为基础的Spring MVC应用容器的配置文件；
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	/*
	 * getServletMappings()方法负责指定需要由DispatcherServlet映射的路径，这里给定的是"/"，
	 * 意思是由DispatcherServlet处理所有向该应用发起的请求
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
