package com.github.bilak.cloud.web.rest;

import com.github.bilak.cloud.caller.HelloCaller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lvasek on 03/11/2016.
 */
@RestController
@RequestMapping("/hello")
public class HelloCallerController {

	private HelloCaller helloCaller;

	public HelloCallerController(HelloCaller helloCaller) {
		this.helloCaller = helloCaller;
	}

	@GetMapping
	public ResponseEntity<String> helo() {
		return ResponseEntity.ok(helloCaller.getHello());
	}
}
