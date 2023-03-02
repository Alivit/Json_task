package ru.clevertec.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.apache.commons.lang3.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class JsonParser {

    private final static List<String> TYPES_PRIMITIVE = List.of("int", "double", "float", "long", "byte", "short", "boolean");
    private final static List<String> TYPES_STR = List.of("String","char");
    private final static List<String> TYPES_PRIMITIVE_ARRAY =  TYPES_PRIMITIVE.stream().map(s -> s+"[]").toList();
    private final static List<String> TYPES_STR_ARRAY =  TYPES_STR.stream().map(s -> s+"[]").toList();
    private final static List<String> TYPES_ARRAY = Stream.concat(TYPES_PRIMITIVE_ARRAY.stream(),TYPES_STR_ARRAY.stream()).toList();
    private final static List<String> TYPES_COLLECTION = List.of("List");

    public static String toJson(Object object) throws IllegalAccessException {
        String jsonStr = "";
        Field[] fields = object.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> field.setAccessible(true));
        for (Field field : fields) {
            System.out.println(field.getType().getSimpleName());
            if (TYPES_PRIMITIVE.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + field.get(object) + ",";
            }
            else if (TYPES_STR.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + "\"" + field.get(object) + "\"" + ",";
            }
            else if (TYPES_ARRAY.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + getArray(field, object) + ",";
            }
            else if (TYPES_COLLECTION.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                jsonStr += "\"" + field.getName() + "\":" + getCollection(field, object) + ",";
            }
            else {
                jsonStr += "\"" + object.getClass().getSimpleName() + "\":[" + toJson(field.get(object)) + "],";
            }
        }
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
        return "{" + jsonStr + "}";
    }

    private static String getArray(Field field, Object object){
        String StrArray = "";
        int i = 0;
        while (true){
            try {
                if(TYPES_PRIMITIVE_ARRAY.stream().anyMatch(type -> type.equals(field.getType().getSimpleName()))) {
                    StrArray += Array.get(field.get(object), i++).toString() + ",";
                }
                else{
                    StrArray += "\"" + Array.get(field.get(object),i++).toString() + "\"" + ",";
                }
            }catch (Exception e){
                break;
            }
        }
        StrArray = StrArray.substring(0,StrArray.length() - 1);
        return "[" + StrArray + "]";
    }

    private static String getCollection(Field field, Object object) throws IllegalAccessException {
        String StrCollection = "";
        for(Object o : (Collection<?>) field.get(object)){
            if(o.getClass().equals(String.class)){
                StrCollection += "\"" + o + "\"" + ",";
            }
            else if(ClassUtils.isPrimitiveOrWrapper(o.getClass())){
                StrCollection += o + ",";
            }
            else {
                StrCollection += "\"" + o.getClass().getSimpleName() + "\":[" + toJson(field.get(object)) + "],";
            }
        }
        StrCollection = StrCollection.substring(0,StrCollection.length() - 1);
        return "[" + StrCollection + "]";
    }

    public static List<?> toObject(File file) throws IOException{
        return newMapper().readValue(file, new TypeReference<>() {
        });

    }

    private static ObjectMapper newMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setLocale(Locale.ENGLISH);
        mapper.registerModule(new JSR310Module());
        return mapper;
    }
}
