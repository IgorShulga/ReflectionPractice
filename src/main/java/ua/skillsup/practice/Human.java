package ua.skillsup.practice;

import java.time.LocalDate;

public class Human {
    private String firsName;
    private String lastName;
    @JsonValue(name = "fun")
    private String hobby;
    private LocalDate birthDate;


    public Human() {
    }

    public Human(String firsName, String lastName, String hobby) {
    }

    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Human{" +
                "firsName='" + firsName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hobby='" + hobby + '\'' +
//                ", birthDate=" + birthDate +
                '}';
    }
}
