package com.github.bilak.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by lvasek on 03/11/2016.
 */
@EnableEurekaClient
@SpringBootApplication
public class InitializerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InitializerServiceApplication.class, args);
	}

}
