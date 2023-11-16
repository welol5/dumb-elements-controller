package com.dumbelements.microcontroller;

import com.dumbelements.beans.BulkLEDStatus;
import com.dumbelements.beans.LEDStatus;

public class ESP32 extends Microcontroller{

    public ESP32(String ip) {
        super(ip);
    }

    @Override
    public String formatMessageBody(BulkLEDStatus status) {

        LEDStatus[] statusArray = status.getStatus();
        StringBuilder builder = new StringBuilder();
        for(LEDStatus s : statusArray){
            builder.append("leds:");
            builder.append(s.getLedStart());
            builder.append("-");
            builder.append(s.getLedEnd());
            builder.append('\n');
            builder.append("r:");
            builder.append(s.getR());
            builder.append('\n');
            builder.append("g:");
            builder.append(s.getG());
            builder.append('\n');
            builder.append("b:");
            builder.append(s.getB());
            builder.append('\n');
        }
        builder.deleteCharAt(builder.lastIndexOf("\n"));

        return builder.toString();
    }
    
}
