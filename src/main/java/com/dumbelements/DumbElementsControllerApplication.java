package com.dumbelements;

import java.io.FileNotFoundException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DumbElementsControllerApplication {

	public static void main(String[] args) {
		try{
			Enviornment.loadEnviornmentVariables();
			Enviornment.createMicrocontrollers();
		} catch (FileNotFoundException e){
			e.printStackTrace();
			//Cannot continue without knowledge of devices
			return;
		}
		SpringApplication.run(DumbElementsControllerApplication.class, args);
	}

}
