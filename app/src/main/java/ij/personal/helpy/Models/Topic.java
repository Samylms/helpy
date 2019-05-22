package ij.personal.helpy.Models;


import java.util.List;

// SUJETS
public class Topic {

    private int idTopic;
    private String title;
    private int idClass;
    private int idSubject;


    public Topic(int idTopic, String title, int idClass, int idSubject) {
        this.idTopic = idTopic;
        this.title = title;
        this.idClass = idClass;
        this.idSubject = idSubject;
    }


    public int getIdTopic() {
        return idTopic;
    }

    public String getTitle() {
        return title;
    }

    public int getIdClass() {
        return idClass;
    }

    public int getIdSubject() {
        return idSubject;
    }

    public Subject getTopicSubject (){
        //todo: api call
        return null;
    }

    public List<Request> getTopicRequests(){
        //todo: api call
        return null;
    }
}
