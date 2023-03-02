package ru.clevertec;

import ru.clevertec.logic.InputLogic;
import ru.clevertec.model.MyClass;
import ru.clevertec.parser.JsonParser;

import java.io.File;


public class Program {
    public static void main(String[] args) throws IllegalAccessException {
        MyClass myClass = new MyClass();
        String result = JsonParser.toJson(myClass);
        InputLogic.addToJson(result,myClass);
        File file = new File("src/main/resources/MyClass.json");
        JsonParser.toObject(file);
    }
}
