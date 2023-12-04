package com.dumbelements.microcontroller;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;

import com.dumbelements.beans.BulkLEDStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RaspberryPi extends Microcontroller{

    public RaspberryPi(String ip, String port) {
        super(ip, port);
        
    }

    @Override
    public BodyPublisher formatMessageBody(BulkLEDStatus status) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return BodyPublishers.ofString(mapper.writeValueAsString(status));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
