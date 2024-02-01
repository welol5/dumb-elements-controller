package com.dumbelements.beans;

public class LEDAnimation {
    
    private String namedAnimation;
    private boolean stopAnimation = false;

    public String getNamedAnimation() {
        return namedAnimation;
    }
    public void setNamedAnimation(String namedAnimation) {
        this.namedAnimation = namedAnimation;
    }
    public boolean isStopAnimation() {
        return stopAnimation;
    }
    public void setStopAnimation(boolean stopAnimation) {
        this.stopAnimation = stopAnimation;
    }
    @Override
    public String toString() {
        return "LEDAnimation [namedAnimation=" + namedAnimation + ", stopAnimation=" + stopAnimation + "]";
    }
}
