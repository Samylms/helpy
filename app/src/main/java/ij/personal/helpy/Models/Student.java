package ij.personal.helpy.Models;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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

    public int getIdClass() {
        return idClass;
    }

    public Class getStudentClass(){
        //todo: api call

        return null;
    }

    public void logStudent(Context context){
        JsonObject json = new JsonObject();
//        json.addProperty("foo", "bar");
        json.addProperty("mail", "jerome.isoard@u-psud.fr");
        json.addProperty("mdp", "prism2019");

        Log.d("debug", json.toString());

        Ion.with(context)
                .load("http://185.225.210.63:3000/eleve/login")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (!e.equals(null)) {
                            Log.d("DEBUG", e.toString());
                        }
                        if (!result.equals(null)) {
                            Log.d("debug", result.getAsJsonObject().toString());
                        }
                    }
                });
        Log.d("debug", "************** student Log In");
    }
}
