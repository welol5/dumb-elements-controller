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

import com.dumbelements.Enviornment;
import com.dumbelements.beans.BulkLEDStatus;
import com.dumbelements.microcontroller.Microcontroller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LEDWorker {

    public boolean updateLEDColors(BulkLEDStatus ledStatus, Microcontroller microcontroller) {

        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            ObjectMapper mapper = new ObjectMapper();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(microcontroller.getControllerURL()))
                    .POST(BodyPublishers.ofString(microcontroller.formatMessageBody(ledStatus)))
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if(response.statusCode() == 200){
                return true;
            } else {
                return false;
            }
        } catch (JsonProcessingException e) {
            // this should never occur as the object is a pojo
            e.printStackTrace();
            return false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    
}
