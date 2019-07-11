package ua.skillsup.practice;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        Human human = new Human();
        JsonParser jsonParser = new JsonParser();

        human.setFirsName("Ivan");
        human.setLastName("Ivanov");
        human.setHobby("Traveling");
        human.setBirthDate(LocalDate.now().minusYears(20));

        System.out.println(jsonParser.toJson(human));
        String json = jsonParser.toJson(human);
        System.out.println(jsonParser.fromJson(json, Human.class));
    }
}
