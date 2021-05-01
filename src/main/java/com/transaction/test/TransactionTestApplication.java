package com.transaction.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.transaction.test.dao")
public class TransactionTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionTestApplication.class, args);
		System.out.println("正在运行");
	}


}
