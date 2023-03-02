package ru.clevertec.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InputLogic {
    public static void addToJson(String jsonStr, Object o){
        List<String> listJson = new ArrayList<>();
        String nameClass = o.getClass().getSimpleName();

        File file = new File("src/main/resources/" + nameClass + ".json");
        if(file.exists() && !file.isDirectory() && file.length() != 0){
            listJson = OutputLogic.readJson(file);
            listJson.add(jsonStr);
        }
        else {
            createFile(file);
            listJson.add(jsonStr);
        }

        listJson.set(0, "[" + listJson.get(0));
        listJson.set(listJson.size() - 1, listJson.get(listJson.size() - 1) + "]");
        List<String> list1 = listJson.stream().limit(listJson.size() - 1).map(list -> list + ",").toList(); // добавление запятых кроме последней строки
        List<String> list2 = listJson.stream().skip(listJson.size() - 1).toList(); // получение последней строки
        listJson = Stream.concat(list1.stream(), list2.stream()).toList(); // объелинение
        try(FileWriter writer = new FileWriter(file, false)){
            PrintWriter print = new PrintWriter(writer);
            listJson.forEach(print::println);
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
