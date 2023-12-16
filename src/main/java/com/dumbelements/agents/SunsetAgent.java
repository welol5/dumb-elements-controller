package com.dumbelements.agents;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dumbelements.Enviornment;
import com.dumbelements.beans.LEDAnimation;
import com.dumbelements.led.LEDWorker;
import com.dumbelements.microcontroller.Microcontroller;

import jakarta.annotation.PostConstruct;

@Component
public class SunsetAgent extends Agent implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(SunsetAgent.class);

    private static final String astronomyAPIURL = "https://aa.usno.navy.mil/api/rstt/oneday?";

    @Autowired private Enviornment env;

    public SunsetAgent(){     
    }

    @PostConstruct
    public void init(){
        logger.info("Starting agent");
        try{
            long offset = getTimeUntilNextSunset();
            if(offset > (24*60*60)){
                offset = getTimeUntilSunset(ZonedDateTime.now());
            }
            scheduler.schedule(this, offset, TimeUnit.SECONDS);
            logger.info("Agent scheduled to run in " + offset + " seconds");
        } catch (IOException | InterruptedException e){
            logger.error("Agent failed to start", e);
        }
    }

    @Override
    public void run() {
        logger.info("Agent running");
        LEDWorker worker = new LEDWorker();
        Microcontroller micro = env.getMicrocontrollers()[0];
        LEDAnimation stars = new LEDAnimation();
        stars.setNamedAnimation("stars");
        worker.runNamedAnimation(micro, stars);

        logger.info("Agent finished tasks, scheduling next run");
        try{
            long offset = getTimeUntilNextSunset();
            if(offset == -1){
                logger.info("Location not set, sunset time cannot be reterived. The agent will not be started. If this was not intentional add \"locationcoords\" to your enviornment");
                return;
            }
            scheduler.schedule(this, offset, TimeUnit.SECONDS);
            logger.info("Next run scheduled in " + offset + " seconds");
        } catch (IOException | InterruptedException e){
            logger.error("Agent failed to schedule", e);
        }
    }

    private long getTimeUntilSunset(ZonedDateTime date) throws IOException, InterruptedException{
        //check location
        String location = env.getVariable("locationcoords");
        if(location == null){
            return -1;
        }

        String dateString = "date=" + date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
        int tz = date.getOffset().getTotalSeconds()/(60*60);

        HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(astronomyAPIURL + dateString + "&coords=" + location + "&tz=" + tz))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        JSONObject resp = new JSONObject(response.body());
        logger.info("url: " + astronomyAPIURL + dateString + "&" + location);
        logger.info(resp.toString());
        JSONArray sunData = resp.getJSONObject("properties").getJSONObject("data").getJSONArray("sundata");
        LocalTime sunsetTime = null;
        for(int i = 0; i < sunData.length(); i++){
            if(sunData.getJSONObject(i).has("phen") && sunData.getJSONObject(i).getString("phen").equals("Set")){
                sunsetTime = LocalTime.parse(sunData.getJSONObject(i).getString("time"));
                break;
            }
        }

        ZonedDateTime sunset = ZonedDateTime.of(date.toLocalDate(), sunsetTime, ZonedDateTime.now().getZone());
		//assumes that this is not being launched beteen 11 and midnight
		return LocalDateTime.now().until(sunset, ChronoUnit.SECONDS);
    }

    private long getTimeUntilNextSunset() throws IOException, InterruptedException{
        ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);
        return getTimeUntilSunset(tomorrow);
    }
    
}
