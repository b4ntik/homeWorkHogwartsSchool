package ru.hogwarts.school.model;

public class Faculty {
    private long id = 0;
    private String name;
    private String color;

    public long getId() {
        return id;
    }

    public void setId() {
        id++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
