package com.dumbelements.led;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.dumbelements.beans.BulkLEDStatus;

@RestController
@RequestMapping(path="/led")
public class LEDController {

    @Autowired private LEDWorker worker;
    
    @RequestMapping(method=POST)
    public ResponseEntity<Void> updateLEDColors(@RequestBody BulkLEDStatus status){

        worker.updateLEDColors(status);

        return ResponseEntity.ok().build();
    }
}
