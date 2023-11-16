package com.dumbelements.beans;

public class LEDUpdateRequest {
    private String name;
    private BulkLEDStatus ledsToUpdate;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BulkLEDStatus getLedsToUpdate() {
        return ledsToUpdate;
    }
    public void setLedsToUpdate(BulkLEDStatus ledsToUpdate) {
        this.ledsToUpdate = ledsToUpdate;
    }
    
    @Override
    public String toString() {
        return "LEDUpdateRequest [name=" + name + ", ledsToUpdate=" + ledsToUpdate + "]";
    }
    
}
