package com.dumbelements.microcontroller;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;

import com.dumbelements.beans.BulkLEDStatus;
import com.dumbelements.beans.LEDStatus;

public class ESP32 extends Microcontroller{

    private final int COMMAND_LENGTH = 7;

    public ESP32(String ip) {
        super(ip);
    }

    public BodyPublisher formatMessageBody(BulkLEDStatus status) {

        LEDStatus[] statusArray = status.getStatus();
        byte[] bytes = new byte[COMMAND_LENGTH*statusArray.length];//[ledStartHigh,ledStartLow,ledEndHigh,ledEndLow,r,g,b]
        for(int i = 0; i < statusArray.length; i++){
            bytes[(i*COMMAND_LENGTH)] = (byte)(statusArray[i].getLedStart()>>8);
            bytes[(i*COMMAND_LENGTH)+1] = (byte)statusArray[i].getLedStart();
            bytes[(i*COMMAND_LENGTH)+2] = (byte)(statusArray[i].getLedEnd()>>8);
            bytes[(i*COMMAND_LENGTH)+3] = (byte)statusArray[i].getLedEnd();
            bytes[(i*COMMAND_LENGTH)+4] = (byte)statusArray[i].getR();
            bytes[(i*COMMAND_LENGTH)+5] = (byte)statusArray[i].getG();
            bytes[(i*COMMAND_LENGTH)+6] = (byte)statusArray[i].getB();
        }
        return BodyPublishers.ofByteArray(bytes);
    }
    
}
