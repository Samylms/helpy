package ij.personal.helpy.Models;

// ELEVES
public class Student {

    private int idStudent;
    private String mail;
    private String pwd;
    private String firstName;
    private String lastName;
    private int phone;
    private int prefPhone;
    private int prefSms;
    private int prefMail;
    private int prefAlertP;
    private int prefAlertG;
    private int idClass;


    public Student(int idStudent, String mail, String pwd, String firstName, String lastName, int phone, int prefPhone, int prefSms, int prefMail, int prefAlertP, int prefAlertG, int idClass) {
        this.idStudent = idStudent;
        this.mail = mail;
        this.pwd = pwd;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.prefPhone = prefPhone;
        this.prefSms = prefSms;
        this.prefMail = prefMail;
        this.prefAlertP = prefAlertP;
        this.prefAlertG = prefAlertG;
        this.idClass = idClass;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getPrefPhone() {
        return prefPhone;
    }

    public void setPrefPhone(int prefPhone) {
        this.prefPhone = prefPhone;
    }

    public int getPrefSms() {
        return prefSms;
    }

    public void setPrefSms(int prefSms) {
        this.prefSms = prefSms;
    }

    public int getPrefMail() {
        return prefMail;
    }

    public void setPrefMail(int prefMail) {
        this.prefMail = prefMail;
    }

    public int getPrefAlertP() {
        return prefAlertP;
    }

    public void setPrefAlertP(int prefAlertP) {
        this.prefAlertP = prefAlertP;
    }

    public int getPrefAlertG() {
        return prefAlertG;
    }

    public void setPrefAlertG(int prefAlertG) {
        this.prefAlertG = prefAlertG;
    }

    public int getIdClass() {
        return idClass;
    }

    public void setIdClass(int idClass) {
        this.idClass = idClass;
    }
}
