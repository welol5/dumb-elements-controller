package com.dumbelements;

import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dumbelements.agents.NightAgent;

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

		setupAgents();

		//run webserver
		SpringApplication.run(DumbElementsControllerApplication.class, args);
	}

	private static void setupAgents(){
		LocalTime lightsOutTime = LocalTime.of(23, 0, 0); 
		//assumes that this is not being launched beteen 11 and midnight
		long offset = LocalTime.now().until(lightsOutTime, ChronoUnit.SECONDS);
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new NightAgent(), offset, (long)(24*60*60), TimeUnit.SECONDS);
	}

}
