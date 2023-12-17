package com.dumbelements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dumbelements.microcontroller.ESP32;
import com.dumbelements.microcontroller.Microcontroller;
import com.dumbelements.microcontroller.RaspberryPi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * This is a crude implementation of a class to deal with all of the constants. The constants
 * are read in from the enviornment.txt file that uses the format "<key>=<value>".
 * 
 * This class will be updated to better interact with constants as it becomes nessicary,
 * however, some of the implementation is being added early.
 */
@Component()
public class Enviornment {

    private static Logger logger = LoggerFactory.getLogger(Enviornment.class);

    private Map<String, String> variables = new HashMap<String, String>();

    private Microcontroller[] microcontrollers;
    private final Map<Integer, Map<String,String>> setups = new HashMap<Integer, Map<String,String>>();

    public Enviornment() throws FileNotFoundException {
        logger.info("initializing enviornemnt");
        // logger.info("Found properties: ");
        // for(Map.Entry o:  System.getProperties().entrySet()){
        //     logger.info(o.getKey() + " : " + o.getValue());
        // }
        loadEnviornmentVariables();
    }

    public Microcontroller[] getMicrocontrollers() {
        return microcontrollers;
    }

    public void loadEnviornmentVariables() throws FileNotFoundException {
        File enviornmentVariablesFile = getEnviornmentFile();
        BufferedReader reader = new BufferedReader(new FileReader(enviornmentVariablesFile));

        String line;
        while (true) {
            try {
                line = reader.readLine();
                if (line == null) {
                    reader.close();
                    break;
                } else if (line.length() == 0) {
                    //ignore empty lines
                    continue;
                } else if(line.charAt(0) == '#'){
                    //ignore comments
                    continue;
                }
                variables.put(line.split("=")[0], line.split("=")[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        createMicrocontrollers();
    }

    public File getEnviornmentFile() throws FileNotFoundException{
        String commandLineFilePath = System.getProperty("enviornmentPath");
        logger.info("Path to enviornment file: " + commandLineFilePath);
        if(commandLineFilePath == null){
            throw new FileNotFoundException("Enviornment must be specified");
        } else {
            File enviormentFile = new File(commandLineFilePath);
            if(enviormentFile.exists()){
                return enviormentFile;
            } else {
                throw new FileNotFoundException("The specified file does not exist: " + enviormentFile.getAbsolutePath());
            }
        }
    }

    public void createMicrocontrollers() {
        ArrayList<Microcontroller> microList = new ArrayList<Microcontroller>();
        Map<String, String> controllerIpaAnPorts = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            if (entry.getKey().matches("microcontroller\\..*")) {
                if (entry.getKey().matches("microcontroller\\.esp32\\..*")) {
                    if (entry.getKey().matches("microcontroller\\.esp32\\.[0-9]+\\.ip")) {
                        microList.add(new ESP32(entry.getValue()));
                    }
                } else if(entry.getKey().matches("microcontroller\\.raspberrypi\\..*")){
                    if(entry.getKey().matches("microcontroller\\.raspberrypi\\.[0-9]+\\.(ip|port)")){
                        if(controllerIpaAnPorts.get(entry.getKey().substring(0, entry.getKey().lastIndexOf("."))) == null){
                            controllerIpaAnPorts.put(entry.getKey().substring(0, entry.getKey().lastIndexOf(".")), entry.getValue());
                        } else if(entry.getKey().matches("microcontroller\\.raspberrypi\\.[0-9]+\\.ip")){
                            microList.add(new RaspberryPi(entry.getValue(), controllerIpaAnPorts.get(entry.getKey().substring(0, entry.getKey().lastIndexOf(".")))));
                            controllerIpaAnPorts.remove(controllerIpaAnPorts.get(entry.getKey().substring(0, entry.getKey().lastIndexOf("."))));
                        } else if(entry.getKey().matches("microcontroller\\.raspberrypi\\.[0-9]+\\.port")){
                            microList.add(new RaspberryPi(controllerIpaAnPorts.get(entry.getKey().substring(0, entry.getKey().lastIndexOf("."))), entry.getValue()));
                            controllerIpaAnPorts.remove(controllerIpaAnPorts.get(entry.getKey().substring(0, entry.getKey().lastIndexOf("."))));
                        }
                    }
                }
            }
        }
        microcontrollers = microList.toArray(new Microcontroller[0]);
        queryMicrocontrollerSetups();
    }

    public void queryMicrocontrollerSetups(){
        Microcontroller[] micros = getMicrocontrollers();
        ObjectMapper mapper = new ObjectMapper();
        for(int i = 0; i < micros.length; i++){
            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(micros[i].getControllerURL()))
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                if(response.statusCode() == 200){
                    MapType typeRef = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, String.class);
                    setups.put(i, mapper.readValue(response.body(), typeRef));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> getMicrocontrollerSetup(int id){
        return setups.get(id);
    }

    public String getVariable(String key) {
        return variables.get(key);
    }

    //TODO add methods that automatically convert variables to correct types
}
