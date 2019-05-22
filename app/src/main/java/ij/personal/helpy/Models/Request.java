package ij.personal.helpy.Models;


// DEMANDES
public class Request {

    private int idTopic;
    private int idStudent;
    private String description;
    private String dateTime;
    private String type;


    public Request(String description, String dateTime, String type, int idTopic, int idStudent) {
        this.description = description;
        this.dateTime = dateTime;
        this.type = type;
        this.idTopic = idTopic;
        this.idStudent = idStudent;
    }


    public String getDescription() {
        return description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdTopic() {
        return idTopic;
    }

    public int getIdStudent() {
        return idStudent;
    }
}
