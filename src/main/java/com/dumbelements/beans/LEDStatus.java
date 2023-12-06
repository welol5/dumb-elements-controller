package com.dumbelements.beans;

public class LEDStatus {
    
    private int ledStart = 0;
    private int ledEnd = 0;
    private int r = 0;
    private int g = 0;
    private int b = 0;

    public int getLedStart() {
        return ledStart;
    }
    public void setLedStart(int ledStart) {
        this.ledStart = ledStart;
    }
    public int getLedEnd() {
        return ledEnd;
    }
    public void setLedEnd(int ledEnd) {
        this.ledEnd = ledEnd;
    }
    public int getR() {
        return r;
    }
    public void setR(int r) {
        this.r = r;
    }
    public int getG() {
        return g;
    }
    public void setG(int g) {
        this.g = g;
    }
    public int getB() {
        return b;
    }
    public void setB(int b) {
        this.b = b;
    }
    @Override
    public String toString() {
        return "LEDStatus [ledStart=" + ledStart + ", ledEnd=" + ledEnd + ", r=" + r + ", g=" + g + ", b=" + b + "]";
    }
}
