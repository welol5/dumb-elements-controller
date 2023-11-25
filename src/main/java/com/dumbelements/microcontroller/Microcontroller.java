package com.dumbelements.microcontroller;

import java.net.http.HttpRequest.BodyPublisher;

import com.dumbelements.beans.BulkLEDStatus;

public abstract class Microcontroller {

    protected boolean isHTTPS;
    protected String ip;
    protected String port;

    protected Microcontroller(String ip, String port, boolean isHTTPS){
        this.isHTTPS = isHTTPS;
        this.ip = ip;
        this.port = port;
    }

    protected Microcontroller(String ip, String port){
        this(ip,port,false);
    }

    protected Microcontroller(String ip, boolean isHTTPS){
        this(ip,null,isHTTPS);
    }

    protected Microcontroller(String ip){
        this(ip,null,false);
    }

    public String getControllerURL(){
        StringBuilder builder = new StringBuilder();

        if(isHTTPS){
            builder.append("https://");
        } else {
            builder.append("http://");
        }

        builder.append(ip);

        if(null != port){
            builder.append(":");
            builder.append(port);
        }

        return builder.toString();
    }

    public abstract BodyPublisher formatMessageBody(BulkLEDStatus status);
}
