package com.github.bilak.cloud.authorization.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lvasek on 03/11/2016.
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

	@GetMapping
	public ResponseEntity<String> helloWorld() {
		return ResponseEntity.ok("Hello world");
	}
}
