package com.newproj.configuration.datasource;


import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import com.newproj.configuration.ProxyResourceResolver;

/**
 * 数据源配置类 .
 * 
 * @author Lain.Cheng
 *
 */
@ConditionalOnClass( { DataSource.class , BasicDataSource.class } )
@EnableConfigurationProperties( DataSourceConfigurationProperties.class )
public class DataSourceConfiguration {

	@Autowired
	private DataSourceConfigurationProperties confProperties ;
	
	@Bean("dataSource")
	public BasicDataSource getDataSource(){
		BasicDataSource dataSource = new BasicDataSource() ;
		confProperties.config( dataSource );
		return dataSource ;
	}
	
	@Autowired
	@Bean("sqlSessionFactory")
	public SqlSessionFactory getSessionFactoryBean( DataSource dataSource ) throws Exception{
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean() ;
		sessionFactory.setDataSource(dataSource);
		Resource [] mybaitsResources = ProxyResourceResolver.getResource("classpath*:com/newproj/core/mybatis/mybatis-config.xml") ;
		if( mybaitsResources == null || mybaitsResources.length == 0 ){
			throw new RuntimeException("Mybatis configuration file is not exists !") ;
		}
		sessionFactory.setConfigLocation(mybaitsResources[0]);
		sessionFactory.setMapperLocations( ProxyResourceResolver.getResource("classpath*:com/newproj/**/mapper/*.xml"));
		return sessionFactory.getObject() ;
	}
	
	@Autowired
	@Bean("sqlSessioinTemplate") 
	public SqlSessionTemplate getSqlSessionTemplate( SqlSessionFactory sessionFactory ){
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate( sessionFactory ) ;
		return sqlSessionTemplate ;
	}
	
	/*@Bean("dbHepler") 
	public DbHelper getDBHepler(){
		DbHelper dbhelper = new DbHelper(this.getDataSource());
		return dbhelper;
	}*/
	
	
}
