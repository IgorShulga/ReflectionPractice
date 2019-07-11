package ua.skillsup.practice;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class JsonParser {

    String toJson(Object object) throws IllegalAccessException {
        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        StringBuilder string = new StringBuilder("{");

        for (Field field : fields) {
            field.setAccessible(true);
            if (Objects.nonNull(field.get(object))) {
                String nameField = field.getName();
                if (field.isAnnotationPresent(JsonValue.class)) {
                    nameField = field.getAnnotation(JsonValue.class).name();
                }
                string.append("\"")
                        .append(nameField)
                        .append("\":\"")
                        .append(field.get(object))
                        .append("\",");
                field.setAccessible(false);
            }
        }

        if (string.lastIndexOf(",") > 0) {
            string.deleteCharAt(string.lastIndexOf(","));
        }
        string.append("}");

        return string.toString();
    }

    public <T> T fromJson(String json, Class<T> clazz)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String[] arrayFieldValue = json.replaceAll("\"", "")
                .replaceAll("\\{", "")
                .replaceAll("}", "")
                .split(",");

        Map<String, String> mapField = Arrays.stream(arrayFieldValue)
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(array -> array[0], array -> array[1]));

        T object = clazz.getConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);
            String nameField = field.getName();
            if (field.isAnnotationPresent(JsonValue.class)) {
                nameField = field.getAnnotation(JsonValue.class).name();
                String value = mapField.get(nameField);
                mapField.put(nameField, value);
            }

            if (mapField.containsKey(nameField)) {
                String value = mapField.get(nameField);
                field.set(object, value);
            }
            field.setAccessible(false);
        }
        return object;
    }
}