package ru.clevertec.logic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class OutputLogic {
    public static String readJson(File file){
        String jsonStr = "";
        try(FileReader reader = new FileReader(file)){
            Scanner scan = new Scanner(reader);
            while (scan.hasNextLine()) {
                jsonStr+=scan.nextLine() + "\n";
            }
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        return jsonStr;
    }
}
