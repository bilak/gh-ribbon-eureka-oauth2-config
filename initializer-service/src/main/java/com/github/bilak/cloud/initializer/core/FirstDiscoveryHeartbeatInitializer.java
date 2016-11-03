package com.github.bilak.cloud.initializer.core;

import com.github.bilak.cloud.caller.HelloCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lvasek on 03/11/2016.
 */
@Component
public class FirstDiscoveryHeartbeatInitializer implements ApplicationListener<HeartbeatEvent> {

	private static final Logger logger = LoggerFactory.getLogger(FirstDiscoveryHeartbeatInitializer.class);
	private static final String separator = "############################################################";

	private AtomicInteger counter = new AtomicInteger(0);

	private DiscoveryClient discoveryClient;

	private HelloCaller helloCaller;

	@Autowired
	public FirstDiscoveryHeartbeatInitializer(HelloCaller helloCaller, DiscoveryClient discoveryClient) {
		this.helloCaller = helloCaller;
		this.discoveryClient = discoveryClient;
	}

	@Override
	public void onApplicationEvent(HeartbeatEvent event) {
		if (this.counter.get() == 0) {
			try {
				for (int i = 0; i < 5; i++) {
					logger.debug(separator);
				}
				List<String> services = discoveryClient.getServices();
				services.forEach(s -> logger.debug("Fond service {}", s));
				List<ServiceInstance> apiServiceInstances = discoveryClient.getInstances("api-service");
				apiServiceInstances.forEach(s -> logger.debug("Api service : {}", s));

				sayHello();
			} catch (Exception e) {
				logger.error("Unable to perform initialization", e);
			}

		}
		if (this.counter.incrementAndGet() == Integer.MAX_VALUE)
			this.counter.set(1);
	}

	private void sayHello() {
		String helloResponse = helloCaller.getHello();
		logger.debug("Output from getHello {}", helloResponse);
	}
}
