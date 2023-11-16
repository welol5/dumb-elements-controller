package com.dumbelements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dumbelements.microcontroller.ESP32;
import com.dumbelements.microcontroller.Microcontroller;

/**
 * This is a crude implementation of a class to deal with all of the constants. The constants
 * are read in from the enviornment.txt file that uses the format "<key>=<value>".
 * 
 * This class will be updated to better interact with constants as it becomes nessicary,
 * however, some of the implementation is being added early.
 */
public class Enviornment {

    private static Map<String, String> variables = new HashMap<String, String>();
    private static String defaultFilePath = "src/main/resources/enviornment.txt";

    private static Microcontroller[] microcontrollers;

    public static Microcontroller[] getMicrocontrollers() {
        return microcontrollers;
    }

    public static void loadEnviornmentVariables() throws FileNotFoundException {
        loadEnviornmentVariables(defaultFilePath);
    }

    public static void loadEnviornmentVariables(String filePath) throws FileNotFoundException {
        File enviornmentVariablesFile = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(enviornmentVariablesFile));

        String line;
        while (true) {
            try {
                line = reader.readLine();
                if (line == null) {
                    reader.close();
                    break;
                }
                variables.put(line.split("=")[0], line.split("=")[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createMicrocontrollers() {
        ArrayList<Microcontroller> microList = new ArrayList<Microcontroller>();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            if (entry.getKey().startsWith("microcontroller")) {
                if (entry.getKey().startsWith("microcontroller.esp32")) {
                    if (entry.getKey().equals("microcontroller.esp32.ip")) {
                        microList.add(new ESP32(entry.getValue()));
                    }
                }
            }
        }
        microcontrollers = microList.toArray(new Microcontroller[0]);
    }

    public static String getVariable(String key) {
        return variables.get(key);
    }

}
