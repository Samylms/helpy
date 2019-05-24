package ij.personal.helpy.Models;

import java.util.ArrayList;
import java.util.List;

// CLASSES
public class Class {

    int id;
    int year;
    String name;
    List<Topic> classTopics;

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

        classTopics = new ArrayList<>();
        Topic topic1 = new Topic(1,"Lab 3", this.id, 4);
        Topic topic2 = new Topic(1,"Lab 2", this.id, 2);
        Topic topic3 = new Topic(1,"Projet Web", this.id, 5);
        Topic topic4 = new Topic(1,"Prototype", this.id, 7);
        classTopics.add(topic1);
        classTopics.add(topic2);
        classTopics.add(topic3);
        classTopics.add(topic4);
        return classTopics;
    }
}
