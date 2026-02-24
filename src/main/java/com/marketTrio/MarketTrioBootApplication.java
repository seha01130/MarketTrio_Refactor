package com.marketTrio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan(basePackages = "com.marketTrio")
@EnableJpaRepositories(basePackages = "com.marketTrio")
public class MarketTrioBootApplication { 
//public class MarketTrioBootApplication extends SpringBootServletInitializer {
	/*
    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JpetstoreBootApplication.class);
	}
	*/
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MarketTrioBootApplication.class, args);
    }
}