package com.dumbelements.beans;

import java.util.Arrays;

public class BulkLEDStatus {

    private LEDStatus[] status;

    public LEDStatus[] getStatus() {
        return status;
    }

    public void setStatus(LEDStatus[] status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BulkLEDStatus [status=" + Arrays.toString(status) + "]";
    }

}
