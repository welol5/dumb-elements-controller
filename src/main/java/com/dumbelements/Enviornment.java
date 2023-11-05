package com.dumbelements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Enviornment {

    private static Map<String,String> variables = new HashMap<String,String>();
    private static String defaultFilePath = "src/main/resources/enviornment.txt";

    public static void loadEnviornmentVariables() throws FileNotFoundException{
        loadEnviornmentVariables(defaultFilePath);
    }

    public static void loadEnviornmentVariables(String filePath) throws FileNotFoundException{
        File enviornmentVariablesFile = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(enviornmentVariablesFile));

        String line;
        while(true){
            try {
                line = reader.readLine();
                if(line == null){
                    reader.close();
                    break;
                }
                variables.put(line.split("=")[0], line.split("=")[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getVariable(String key){
        return variables.get(key);
    }
    
}
