package ij.personal.helpy.Models;


import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

// SUJETS
public class Topic {

    private int idTopic;
    private String title;
    private int idClass;
    private int idSubject;
    private String subjectName;

    private List<Request> topicRequests;

    //handle json
    private JsonObject jsonResponce;


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

    public String getTopicSubjectName (Context context){
//        try {
//            jsonResponce = Ion.with(context)
//                    .load("http://185.225.210.63:3000/matiere/" + String.valueOf(this.getIdSubject()))
//                    .asJsonObject()
//                    .get();
//            Log.d("DEBUG", "jsonResponce: OK");
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//            Log.d("DEBUG", "executionException");
//            Log.d("DEBUG", e.toString());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            Log.d("DEBUG", "InterruptionException");
//            Log.d("DEBUG", e.toString());
//        }

//        if (jsonResponce != null) {
//            subjectName = jsonResponce.get("matiere").getAsJsonArray().get(0)
//                    .getAsJsonObject().get("libelle").getAsString();
//        }

        // if server is killed (only for test)
        if (subjectName == null){
            switch (idSubject){
                case 1: subjectName = "Programmation Android"; break;
                case 2: subjectName = "Programmation Web"; break;
                case 3: subjectName = "IHM"; break;
            }
        }
        return subjectName;
    }

    public List<Request> getTopicRequests(Context context){
        try {
            jsonResponce = Ion.with(context)
                    .load("http://185.225.210.63:3000/demande/sujet/" + String.valueOf(this.getIdTopic()))
                    .asJsonObject()
                    .get();
            Log.d("DEBUG", "jsonResponce: OK");

        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("DEBUG", "executionException");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("DEBUG", "InterruptionException");
        }

        topicRequests = new ArrayList<>();
        JsonArray jsonArrayRequests = jsonResponce.get("demande").getAsJsonArray();

        for (JsonElement jsonRequest: jsonArrayRequests) {

            int idStudent = jsonRequest.getAsJsonObject().get("eleveId").getAsInt();
            String description = jsonRequest.getAsJsonObject().get("description").getAsString();
            String dateTime = jsonRequest.getAsJsonObject().get("dateheure").getAsString();
            String type = jsonRequest.getAsJsonObject().get("type").getAsString();
            Request request = new Request(this.idTopic, idStudent, description, dateTime, type);

            topicRequests.add(request);

        }
        Log.d("debug" , String.valueOf(this.idTopic));
        Log.d("debug", topicRequests.toString());
        return topicRequests;
    }

    public void addRequestOnThisTopic (Context context, int idStudent, String type){
        JsonObject json = new JsonObject();
//        json.addProperty("foo", "bar");
//        json.addProperty("sujetId", this.getIdTopic());
        json.addProperty("sujetId", 1);
//        json.addProperty("eleveId", idStudent);
        json.addProperty("eleveId", 1);
        json.addProperty("description", "hello Coco! Help me pleeeeease!");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        json.addProperty("dateheure", currentDateandTime);
        json.addProperty("type", type);
        json.addProperty("flag", 1);

        Log.d("debug", json.toString());

        Ion.with(context)
                .load("http://185.225.210.63:3000/demande/")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d("DEBUG", e.toString());
                        }
                        if (result != null) {
                            Log.d("debug", result.getAsJsonObject().toString());
                        }
                    }
                });
        Log.d("debug", "************** request adding");
    }
}
