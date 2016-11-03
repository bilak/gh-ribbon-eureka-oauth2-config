package com.github.bilak.cloud.caller;

import com.github.bilak.cloud.configuration.SecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

/**
 * Created by lvasek on 03/11/2016.
 */
@Component
public class HelloCaller {

	private static final Logger logger = LoggerFactory.getLogger(HelloCaller.class);

	private RestOperations restTemplate;

	@Autowired
	public HelloCaller(@Qualifier(SecurityConfiguration.LOAD_BALANCED_TEMPLATE) RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getHello() {
		ResponseEntity<String> helloResponse = restTemplate.getForEntity("http://uaa-service/uaa/hello", String.class);
		logger.debug("Output from getHello {}", helloResponse);
		return helloResponse.getBody() != null ? helloResponse.getBody() : "Nobody wants to say getHello";
	}
}
