package ij.personal.helpy.Models;

// ELEVES
public class Student {

    private int idStudent;
    private String mail;
    private String pwd;
    private String firstName;
    private String lastName;
    private int phone;
    private int idClass;


    public Student(int idStudent, String mail, String pwd, String firstName, String lastName, int phone, int idClass) {
        this.idStudent = idStudent;
        this.mail = mail;
        this.pwd = pwd;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.idClass = idClass;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public String getMail() {
        return mail;
    }

    public String getPwd() {
        return pwd;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPhone() {
        return phone;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setIdClass(int idClass) {
        this.idClass = idClass;
    }

    public int getIdClass() {
        return idClass;
    }

    public Class getStudentClass(){
        //todo: api call

        return null;
    }
}
