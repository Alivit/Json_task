package ru.clevertec;

import org.json.simple.parser.ParseException;
import ru.clevertec.logic.InputLogic;
import ru.clevertec.model.MyClass;
import ru.clevertec.model.Person;
import ru.clevertec.parser.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Program {
    public static void main(String[] args) throws IOException, IllegalAccessException {
        Person person = new Person();
        String result = JsonParser.toJson(person);

        InputLogic.addToJson(result,person);

        File file = new File("src/main/resources/Person.json");
        List<Person> newPerson = (List<Person>) JsonParser.toObject(file);
        System.out.println(newPerson);
    }
}
