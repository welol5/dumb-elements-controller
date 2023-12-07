package com.dumbelements.beans;

public class LEDUpdateRequest {
    private String name;
    private BulkLEDStatus ledsToUpdate;
    private LEDAnimation ledAnimation;

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
    public LEDAnimation getLedAnimation() {
        return ledAnimation;
    }
    public void setLedAnimation(LEDAnimation ledAnimation) {
        this.ledAnimation = ledAnimation;
    }
    
    @Override
    public String toString() {
        return "LEDUpdateRequest [name=" + name + ", ledsToUpdate=" + ledsToUpdate + ", ledAnimation=" + ledAnimation
                + "]";
    }
}
