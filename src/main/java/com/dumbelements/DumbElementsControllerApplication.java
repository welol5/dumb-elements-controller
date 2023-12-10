package com.dumbelements;

import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dumbelements.agents.NightAgent;

@SpringBootApplication
public class DumbElementsControllerApplication {

	private static Logger logger = LogManager.getLogger(DumbElementsControllerApplication.class);

	public static void main(String[] args) {
		String enviornmentFilePath = null;

		if(args.length > 0){
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-e") && i+1 < args.length){
					enviornmentFilePath = args[i+1];
				}
			}
		}

		try{
			if(enviornmentFilePath != null){
				logger.info("Loading enviornment variables from: " + enviornmentFilePath);
				Enviornment.loadEnviornmentVariables(enviornmentFilePath);
			} else {
				logger.info("Loading enviornment variables from default path");
				Enviornment.loadEnviornmentVariables();
			}
			logger.info("Enviornment variables loaded");
			logger.info("Createing microcontrollers");
			Enviornment.createMicrocontrollers();
		} catch (FileNotFoundException e){
			e.printStackTrace();
			logger.error("Error during enviornment loading", e);
			//Cannot continue without knowledge of devices
			return;
		}

		setupAgents();

		//run webserver
		SpringApplication.run(DumbElementsControllerApplication.class, args);
	}

	private static void setupAgents(){
		logger.info("Starting agents");
		LocalTime lightsOutTime = LocalTime.of(23, 0, 0); 
		//assumes that this is not being launched beteen 11 and midnight
		long offset = LocalTime.now().until(lightsOutTime, ChronoUnit.SECONDS);
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new NightAgent(), offset, (long)(24*60*60), TimeUnit.SECONDS);
		logger.info("Agents started");
	}

}
