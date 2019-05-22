package ij.personal.helpy.Models;

// MATIERES
public class Subject {

    private int idSubject;
    private String name;


    public Subject(int idSubject, String name) {
        this.idSubject = idSubject;
        this.name = name;
    }


    public int getIdSubject() {
        return idSubject;
    }

    public String getName() {
        return name;
    }
}
