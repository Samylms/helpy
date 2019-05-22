package ij.personal.helpy.Models;

import java.util.List;

// CLASSES
public class Class {

    int id;
    int year;
    String name;

    public Class(int id, int year, String name) {
        this.id = id;
        this.year = year;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Topic> getClassTopics (){
        //todo: api call
        return null;
    }
}
