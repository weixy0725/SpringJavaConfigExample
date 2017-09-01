package com.example.database.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description 基本的数据库配置类，使用采用JTA进行Hibernate的事务管理，JPA进行Hibernate的实体映射
 * @author xinyuan.wei
 * @time 2017年9月1日 上午9:25:45
 */
@Configuration
@EnableTransactionManagement
public class BasicDbConfig {

}
