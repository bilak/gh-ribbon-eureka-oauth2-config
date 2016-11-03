package com.github.bilak.cloud.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lvasek on 09/09/16.
 */
@SpringBootApplication
@RestController
@EnableEurekaClient
public class AuthorizationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServiceApplication.class, args);
	}

	@GetMapping({ "/user", "me" })
	public Map<String, String> userInfo(Principal principal) {
		Map<String, String> userInfo = new LinkedHashMap<>();
		if (principal != null) {
			if (!StringUtils.isEmpty(principal.getName()))
				userInfo.put("name", principal.getName());
		}
		return userInfo;
	}
}
