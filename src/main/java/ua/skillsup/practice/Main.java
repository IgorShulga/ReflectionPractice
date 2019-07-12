package ua.skillsup.practice;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ParseException, NoSuchFieldException {
        Human human = new Human();
        JsonParser jsonParser = new JsonParser();

        human.setFirsName("Ivan");
        human.setLastName("Ivanov");
        human.setHobby("Traveling");
        human.setBirthDate(LocalDate.now().minusYears(20));
        human.setRegistrationDate(LocalDate.now().minusYears(1));

        System.out.println("Source object - " + human);
        System.out.println("JSON object - " + jsonParser.toJson(human));
        String json = jsonParser.toJson(human);
        System.out.println("Object from JSON - " + jsonParser.fromJson(json, Human.class));
    }
}
