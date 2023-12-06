package com.ssafy.web.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(value = "com.ssafy.web.*.model.mapper", sqlSessionFactoryRef="myTripSqlSessionFactory")
public class EnjoyTripDatabaseConfig {
	
	@Bean(name="myTripDataSource")
	@ConfigurationProperties(prefix="spring.enjdb.datasource") //appliction.properties 참고.
	public DataSource myTripDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="myTripSqlSessionFactory")
	public SqlSessionFactory myTripSessionFactory(@Qualifier("myTripDataSource") DataSource db1DataSource, ApplicationContext applicationContext) throws Exception{
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(db1DataSource);
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mybatis/mappers/*.xml")); //쿼리작성용 mapper.xml위치 설정.
		return sessionFactory.getObject();
	}
	
	@Bean(name="myTripSqlSessionTemplate")
	public SqlSessionTemplate myTripSqlSessionTemplate(@Qualifier("myTripSqlSessionFactory")SqlSessionFactory myTripSqlSessionFactory) throws Exception{
		return new SqlSessionTemplate(myTripSqlSessionFactory);
	}
	
    @Bean(name = "myTriptransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("myTripDataSource") DataSource db1DataSource) {
        return new DataSourceTransactionManager(db1DataSource);
    }
	
}
