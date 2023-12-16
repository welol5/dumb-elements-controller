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
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dumbelements.DumbElementsControllerApplication;
import com.dumbelements.Enviornment;
import com.dumbelements.beans.LEDAnimation;
import com.dumbelements.led.LEDWorker;
import com.dumbelements.microcontroller.Microcontroller;

@Component
public class SunsetAgent extends Agent implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(DumbElementsControllerApplication.class);

    private static final String astronomyAPIURL = "https://aa.usno.navy.mil/api/rstt/oneday?";

    public SunsetAgent(){
        try{
            long offset = getTimeUntilNextSunset();
            scheduler.schedule(this, offset, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e){
            //TODO add exception handleing
        }
        logger.info("agent started");
    }

    @Override
    public void run() {
        LEDWorker worker = new LEDWorker();
        Microcontroller micro = Enviornment.getMicrocontrollers()[0];
        LEDAnimation stars = new LEDAnimation();
        stars.setNamedAnimation("stars");
        worker.runNamedAnimation(micro, stars);

        try{
            long offset = getTimeUntilNextSunset();
            scheduler.schedule(this, offset, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e){
            //TODO add exception handleing
        }
    }

    private long getTimeUntilNextSunset() throws IOException, InterruptedException{
        String location = Enviornment.getVariable("locationcoords");
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String dateString = "date=" + tomorrow.getYear() + "-" + tomorrow.getMonthValue() + "-" + tomorrow.getDayOfMonth();

        HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(astronomyAPIURL + dateString + "&coords=" + location))
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

        LocalDateTime tomorrowSunset = LocalDateTime.of(tomorrow, sunsetTime);
		//assumes that this is not being launched beteen 11 and midnight
		return LocalDateTime.now().until(tomorrowSunset, ChronoUnit.SECONDS);
    }
    
}
