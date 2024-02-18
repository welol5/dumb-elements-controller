package com.dumbelements.agents;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dumbelements.Enviornment;
import com.dumbelements.led.LEDWorker;
import com.dumbelements.microcontroller.Microcontroller;

import jakarta.annotation.PostConstruct;

@Component
public class NightAgent extends Agent implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(NightAgent.class);

    @Autowired private Enviornment env;

    public NightAgent(){
        
    }

    @PostConstruct
    public void init(){
        logger.info("Starting agent");
        long timeTillRun;
        LocalTime nightAgentRunTime = LocalTime.of(23, 0, 0);
        if(LocalTime.now().getHour() >= nightAgentRunTime.getHour()){
            timeTillRun = LocalDateTime.now().until(LocalDateTime.of(LocalDate.now().plusDays(1), nightAgentRunTime), ChronoUnit.SECONDS);
        } else {
            timeTillRun = LocalDateTime.now().until(LocalDateTime.of(LocalDate.now(), nightAgentRunTime), ChronoUnit.SECONDS);
        }
        scheduler.scheduleAtFixedRate(this, timeTillRun, 24*60*60, TimeUnit.SECONDS);
        logger.info("Agent scheduled to run in " + timeTillRun + " seconds. " + LocalDateTime.now().plus(timeTillRun, ChronoUnit.SECONDS));
    }

    @Override
    public void run() {
        logger.info("Agent running");
        LEDWorker worker = new LEDWorker();
        Microcontroller micro = env.getMicrocontrollers()[0];
        worker.stopAnimation(micro);
        logger.info("Agent finished tasks");
    }
    
}
