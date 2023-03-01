package ru.clevertec.parser;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class JsonParser {

    private final static List<String> TYPES_PRIMITIVE = List.of("int", "double", "float", "long", "byte", "short", "boolean");
    private final static List<String> TYPES_PRIMITIVE_ARRAY =  TYPES_PRIMITIVE.stream().map(s -> s+"[]").toList();
    private final static List<String> TYPES_STR = List.of("String","char");
    private final static List<String> TYPES_STR_ARRAY =  TYPES_STR.stream().map(s -> s+"[]").toList();
    private final static List<String> TYPES_COLLECTION = List.of("Map", "List");

    public static String toJson(Object o) throws IllegalAccessException {
        String jsonStr = "{";
        Field[] fields = o.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> field.setAccessible(true));
        for (Field field : fields) {
            System.out.println(field.getType().getSimpleName());
            if (TYPES_PRIMITIVE.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + field.get(o) + ",";
            }
            else if (TYPES_STR.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + "\"" + field.get(o) + "\"" + ",";
            }
            else if (TYPES_PRIMITIVE_ARRAY.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + getArrayPrimitive(field.get(o)) + ",";
            }
            else if (TYPES_STR_ARRAY.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + getArrayStr(field.get(o)) + ",";
            }
            else {
                jsonStr += "\"" + o.getClass().getSimpleName() + "\":[" + toJson(field.get(o)) + "],";
            }
        }
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
        jsonStr += "}";
        return jsonStr;
    }
    public static String getArrayPrimitive(Object o){
        String StrArray = "";
        int i = 0;
        while (true){
            try {
                StrArray += Array.get(o,i++).toString() + ",";
            }catch (Exception e){
                break;
            }
        }
        StrArray = StrArray.substring(0,StrArray.length() - 1);
        return "[" + StrArray + "]";
    }
    public static String getArrayStr(Object o){
        String StrArray = "";
        int i = 0;
        while (true){
            try {
                StrArray += "\"" + Array.get(o,i++).toString() + "\"" + ",";
            }catch (Exception e){
                break;
            }
        }
        StrArray = StrArray.substring(0,StrArray.length() - 1);
        return "[" + StrArray + "]";
    }
    public static void addToJson(String jsonStr, Object o){
        String jsonStrOld = "";
        String nameClass = o.getClass().getSimpleName();
        File file = new File("src/main/resources/" + nameClass + ".json");
        if(file.exists() && !file.isDirectory() && file.length() != 0){
            try(FileReader reader = new FileReader(file)){
                Scanner scan = new Scanner(reader);
                while (scan.hasNextLine()) {
                    jsonStrOld+=scan.nextLine() + "\n";
                }
                if(jsonStr.length() != 1){
                    jsonStrOld = jsonStrOld.substring(0, jsonStrOld.length() - 2);
                    jsonStrOld += ",\n";
                }
            }
            catch (IOException ex){
                System.out.println(ex.getMessage());
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
