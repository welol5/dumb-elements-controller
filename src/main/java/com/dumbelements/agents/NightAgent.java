package com.dumbelements.agents;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dumbelements.Enviornment;
import com.dumbelements.led.LEDWorker;
import com.dumbelements.microcontroller.Microcontroller;

@Component
public class NightAgent extends Agent implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(NightAgent.class);

    public NightAgent(){
        logger.info("Starting agent");
        long timeTillRun;
        LocalTime nightAgentRunTime = LocalTime.of(23, 0, 0);
        if(LocalTime.now().getHour() >= nightAgentRunTime.getHour()){
            timeTillRun = LocalDateTime.now().until(LocalDateTime.of(LocalDate.now().plusDays(1), nightAgentRunTime), ChronoUnit.SECONDS);
        } else {
            timeTillRun = LocalDateTime.now().until(LocalDateTime.of(LocalDate.now(), nightAgentRunTime), ChronoUnit.SECONDS);
        }
        scheduler.scheduleAtFixedRate(this, timeTillRun, 24*60*60, TimeUnit.SECONDS);
        logger.info("Agent scheduled to run in " + timeTillRun + " seconds");
    }

    @Override
    public void run() {
        logger.info("Agent running");
        LEDWorker worker = new LEDWorker();
        Microcontroller micro = Enviornment.getMicrocontrollers()[0];
        worker.off(micro);
        logger.info("Agent finished tasks");
    }
    
}
