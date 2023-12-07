package com.dumbelements.agents;

import com.dumbelements.Enviornment;
import com.dumbelements.led.LEDWorker;
import com.dumbelements.microcontroller.Microcontroller;

public class NightAgent  implements Runnable{

    @Override
    public void run() {
        LEDWorker worker = new LEDWorker();
        Microcontroller micro = Enviornment.getMicrocontrollers()[0];
        worker.off(micro);
    }
    
}
