package com.study.boardprojectadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BoardProjectAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectAdminApplication.class, args);
	}

}
