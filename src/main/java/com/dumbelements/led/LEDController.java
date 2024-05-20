package com.dumbelements.led;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

import com.dumbelements.Enviornment;
import com.dumbelements.beans.LEDAnimation;
import com.dumbelements.beans.LEDUpdateRequest;

@RestController
@RequestMapping(path="/led")
@CrossOrigin
public class LEDController {

    private Logger logger = LoggerFactory.getLogger(LEDController.class);

    @Autowired private Enviornment env;

    @Autowired private LEDWorker worker;
    
    @RequestMapping(method=POST)
    public ResponseEntity<Void> updateLEDColors(HttpServletRequest request, @RequestBody LEDUpdateRequest updateRequest){
        logger.info("LED update request from: " + request.getRemoteAddr());
        LEDAnimation ledUpdateRequest = new LEDAnimation();
        ledUpdateRequest.setNamedAnimation("static");
        ledUpdateRequest.setCommand(updateRequest.getLedsToUpdate().getStatus());
        return playLEDAnimation(ledUpdateRequest);
    }

    @RequestMapping(method=POST, path="/animation")
    public ResponseEntity<Void> playLEDAnimation(@RequestBody LEDAnimation animationRequest){
        System.out.println(animationRequest);
        Boolean success = worker.runNamedAnimation(env.getMicrocontrollers()[0], animationRequest);
        if(success){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(method=POST, path="/off")
    public ResponseEntity<Void> turnOffLEDs(){
        Boolean success = worker.off(env.getMicrocontrollers()[0]);
        if(success){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
