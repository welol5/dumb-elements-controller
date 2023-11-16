package com.dumbelements.led;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.dumbelements.Enviornment;
import com.dumbelements.beans.LEDUpdateRequest;

@RestController
@RequestMapping(path="/led")
@CrossOrigin
public class LEDController {

    @Autowired private LEDWorker worker;
    
    @RequestMapping(method=POST)
    public ResponseEntity<Void> updateLEDColors(@RequestBody LEDUpdateRequest updateRequest){
        try{
            System.out.println(updateRequest.toString());
            worker.updateLEDColors(updateRequest.getLedsToUpdate(), Enviornment.getMicrocontrollers()[0]);
        } catch (NullPointerException e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
}
