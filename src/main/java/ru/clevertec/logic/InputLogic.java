package ru.clevertec.logic;

import java.io.*;
import java.util.Scanner;

public class InputLogic {
    public static void addToJson(String jsonStr, Object o){
        String jsonStrOld = "";
        String nameClass = o.getClass().getSimpleName();
        File file = new File("src/main/resources/" + nameClass + ".json");
        if(file.exists() && !file.isDirectory() && file.length() != 0){
            jsonStrOld = OutputLogic.readJson(file);
            if(jsonStrOld.length() != 1){
                jsonStrOld = jsonStrOld.substring(0, jsonStrOld.length() - 2);
                jsonStrOld += ",\n";
            }
        }
        else {createFile(file);}

        if(jsonStrOld.length() != 0) jsonStrOld += jsonStr + "]";
        else jsonStrOld = "[" + jsonStr + "]";

        try(FileWriter writer = new FileWriter(file, false)){
            PrintWriter print = new PrintWriter(writer);
            print.println(jsonStrOld);
            print.close();
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    private static void createFile(File file) {
        try {
            file.createNewFile();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
