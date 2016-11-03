package com.github.bilak.cloud.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by lvasek on 03/11/2016.
 */
@EnableEurekaServer
@SpringBootApplication
public class DiscoverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoverServiceApplication.class, args);
	}
}
