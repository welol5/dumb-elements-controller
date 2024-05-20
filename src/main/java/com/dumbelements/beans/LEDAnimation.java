package com.dumbelements.beans;

public class LEDAnimation {
    
    private String namedAnimation;
    private Object command;

    public String getNamedAnimation() {
        return namedAnimation;
    }
    public void setNamedAnimation(String namedAnimation) {
        this.namedAnimation = namedAnimation;
    }
    public Object getCommand() {
        return command;
    }
    public void setCommand(Object command) {
        this.command = command;
    }
    @Override
    public String toString() {
        return "LEDAnimation [namedAnimation=" + namedAnimation + ", command=" + command + "]";
    }

    
}
