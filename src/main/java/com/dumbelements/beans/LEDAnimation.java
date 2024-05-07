package com.dumbelements.beans;

public class LEDAnimation {
    
    private String namedAnimation;

    public String getNamedAnimation() {
        return namedAnimation;
    }
    public void setNamedAnimation(String namedAnimation) {
        this.namedAnimation = namedAnimation;
    }

    @Override
    public String toString() {
        return "LEDAnimation [namedAnimation=" + namedAnimation + "]";
    }
}
