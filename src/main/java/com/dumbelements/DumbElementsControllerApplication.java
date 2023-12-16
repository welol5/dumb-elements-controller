package com.dumbelements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class DumbElementsControllerApplication extends SpringBootServletInitializer {

	private static Logger logger = LoggerFactory.getLogger(DumbElementsControllerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DumbElementsControllerApplication.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DumbElementsControllerApplication.class);
    }

}
