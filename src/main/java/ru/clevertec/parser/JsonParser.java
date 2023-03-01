package ru.clevertec.parser;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class JsonParser {

    private final static List<String> TYPES_PRIMITIVE = List.of("int", "double", "float", "long", "byte", "short", "boolean");
    private final static List<String> TYPES_STR = List.of("String","char");
    private final static List<String> TYPES_PRIMITIVE_ARRAY =  TYPES_PRIMITIVE.stream().map(s -> s+"[]").toList();
    private final static List<String> TYPES_STR_ARRAY =  TYPES_STR.stream().map(s -> s+"[]").toList();
    private final static List<String> TYPES_ARRAY = Stream.concat(TYPES_PRIMITIVE_ARRAY.stream(),TYPES_STR_ARRAY.stream()).toList();
    private final static List<String> TYPES_COLLECTION = List.of("Map", "List");

    public static String toJson(Object o) throws IllegalAccessException {
        String jsonStr = "";
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
            else if (TYPES_ARRAY.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + getArray(field, o) + ",";
            }
            else {
                jsonStr += "\"" + o.getClass().getSimpleName() + "\":[" + toJson(field.get(o)) + "],";
            }
        }
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
        return "{" + jsonStr + "}";
    }

    public static String getArray(Field field, Object o){
        String StrArray = "";
        int i = 0;
        while (true){
            try {
                if(TYPES_PRIMITIVE_ARRAY.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                    StrArray += Array.get(field.get(o), i++).toString() + ",";
                }
                else{
                    StrArray += "\"" + Array.get(o,i++).toString() + "\"" + ",";
                }
            }catch (Exception e){
                break;
            }
        }
        StrArray = StrArray.substring(0,StrArray.length() - 1);
        return "[" + StrArray + "]";
    }
}
