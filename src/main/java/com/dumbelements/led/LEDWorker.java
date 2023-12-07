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

import org.springframework.stereotype.Service;

import com.dumbelements.beans.BulkLEDStatus;
import com.dumbelements.beans.LEDAnimation;
import com.dumbelements.microcontroller.Microcontroller;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class LEDWorker {

    public boolean updateLEDColors(Microcontroller microcontroller, BulkLEDStatus ledStatus) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(30))
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

        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(microcontroller.getControllerURL() + "/led/animation"))
                    .POST(microcontroller.formatMessageBody(ledAnimation))
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if(response.statusCode() == 200){
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean off(Microcontroller microcontroller){
        System.out.println("sending off command");
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(microcontroller.getControllerURL() + "/led/off"))
                    .POST(BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if(response.statusCode() == 200){
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
