package com.dumbelements.agents;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.dumbelements.Enviornment;
import com.dumbelements.led.LEDWorker;
import com.dumbelements.microcontroller.Microcontroller;

@Component
public class NightAgent extends Agent implements Runnable{

    public NightAgent(){
        long timeTillRun = LocalDateTime.now().until(LocalTime.of(23,0,0), ChronoUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this, timeTillRun, 24*60*60, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        LEDWorker worker = new LEDWorker();
        Microcontroller micro = Enviornment.getMicrocontrollers()[0];
        worker.off(micro);
    }
    
}
