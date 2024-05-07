package com.dumbelements.led;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dumbelements.Enviornment;
import com.dumbelements.beans.BulkLEDStatus;
import com.dumbelements.beans.LEDAnimation;
import com.dumbelements.microcontroller.Microcontroller;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class LEDWorker {

    private static Logger logger = LoggerFactory.getLogger(LEDWorker.class);

    @Autowired Enviornment env;

    public boolean updateLEDColors(Microcontroller microcontroller, BulkLEDStatus ledStatus) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(Integer.valueOf(env.getVariable("microcontroller.timeout"))))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(microcontroller.getControllerURL() + "/led"))
                    .POST(microcontroller.formatMessageBody(ledStatus))
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return true;
            }
        } catch (JsonProcessingException e) {
            // this should never occur as the object is a pojo
            e.printStackTrace();
            return false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean runNamedAnimation(Microcontroller microcontroller, LEDAnimation ledAnimation) {
        logger.info("attempting to run animation: " + ledAnimation.getNamedAnimation());

        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(Integer.valueOf(env.getVariable("microcontroller.timeout"))))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(microcontroller.getControllerURL() + "/led/animation"))
                    .POST(microcontroller.formatMessageBody(ledAnimation))
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if(response.statusCode() == 200){
                logger.info("Successfully started animation");
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Error running animation", e);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("Error running animation", e);
        }
        logger.info("Failed to start animation");
        return false;
    }

    public boolean stopAnimation(Microcontroller microcontroller) {
        logger.info("Stopping animation");
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(Integer.valueOf(env.getVariable("microcontroller.timeout"))))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(microcontroller.getControllerURL() + "/led/off"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if(response.statusCode() == 200){
                logger.info("Successfully stopped animation");
                return true;
            } else {
                logger.info("Failed to stop animation. Response status code: " + response.statusCode());
                return false;
            }
        } catch (IOException e) {
            logger.error("Error stopping animation", e);
        } catch (InterruptedException e) {
            logger.error("Error stopping animation", e);
        }
        logger.info("Failed to stop animation");
        return false;
    }

    public boolean off(Microcontroller microcontroller){
        logger.info("Turning off LEDs");
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(Integer.valueOf(env.getVariable("microcontroller.timeout"))))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(microcontroller.getControllerURL() + "/led/off"))
                    .POST(BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if(response.statusCode() == 200){
                logger.info("Successfully shut off LEDs");
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("Failed to shut off LEDs");
        return false;
    }
}
