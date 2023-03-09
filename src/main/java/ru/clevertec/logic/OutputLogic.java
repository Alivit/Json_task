package ru.clevertec.logic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OutputLogic {
    public static List<String> readJson(File file){
        List<String> list = new ArrayList<>();
        try(FileReader reader = new FileReader(file)){
            Scanner scan = new Scanner(reader);
            while (scan.hasNextLine()) {
                String newStr = scan.nextLine();
                list.add(newStr.substring(0,newStr.length() - 1));
            }
            if(list.size() != 0) {
                list.set(0, list.get(0).substring(1)); // убирает квадратную скобку в первой строке
            }
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        return list;
    }
}
