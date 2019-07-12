package ua.skillsup.practice;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
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
                Object valueField = field.get(object);

                if (field.isAnnotationPresent(JsonValue.class)) {
                    nameField = field.getAnnotation(JsonValue.class).name();
                }

                if (field.isAnnotationPresent(CustomDateFormat.class)) {
                    String format = field.getAnnotation(CustomDateFormat.class).format();
                    if (field.get(object) instanceof LocalDate) {
                        valueField = ((LocalDate) field.get(object)).format(DateTimeFormatter.ofPattern(format));
                    }
                }

                string.append("\"")
                        .append(nameField)
                        .append("\":\"")
                        .append(valueField)
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
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ParseException, NoSuchFieldException {
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
            String valueField = mapField.get(nameField);

            if (Objects.nonNull(nameField)) {

                if (field.isAnnotationPresent(JsonValue.class)) {
                    nameField = field.getAnnotation(JsonValue.class).name();
                    valueField = mapField.get(nameField);
                }

                if (mapField.containsKey(nameField)) {
                    if (field.isAnnotationPresent(CustomDateFormat.class)) {
                        String format = field.getAnnotation(CustomDateFormat.class).format();
                        String stringDate = deserializeStringToLocaldate(mapField.get(nameField), format);
                        LocalDate parseDate = LocalDate.parse(stringDate);
                        field.set(object, parseDate);
                    } else if (field.getType().equals(LocalDate.class)) {
                        String stringDate = deserializeStringToLocaldate(mapField.get(nameField), "yyyy-MM-dd");
                        LocalDate parse = LocalDate.parse(stringDate);
                        field.set(object, parse);
                    } else {
                        field.set(object, valueField);
                    }
                }
            }
            field.setAccessible(false);
        }
        return object;
    }

    public String deserializeStringToLocaldate(String string, String format) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat(format);
        Date date = parser.parse(string);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}