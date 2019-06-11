package ij.personal.helpy.Models;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;

// SUJETS
public class Topic {

    public static final String BASE_URL = "http://54.37.157.172:3000";
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

    // API CALL
    public String getTopicSubjectName(Context context) {
        if (Prefs.isServerOK(context)) {
            try {
                jsonResponce = Ion.with(context)
                        .load( BASE_URL + "/matiere/" + String.valueOf(this.getIdSubject()))
                        .asJsonObject()
                        .get();
                Log.d("DEBUG", "jsonResponce: OK");

            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.d("DEBUG", "executionException");
                Log.d("DEBUG", e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("DEBUG", "InterruptionException");
                Log.d("DEBUG", e.toString());
            }

            if (jsonResponce != null) {
                subjectName = jsonResponce.get("matiere").getAsJsonArray().get(0)
                        .getAsJsonObject().get("libelle").getAsString();
            }
        } else {
            // if server is killed (only for test)
            switch (idSubject) {
                case 1:
                    subjectName = "Programmation Android";
                    break;
                case 2:
                    subjectName = "Programmation Web";
                    break;
                case 3:
                    subjectName = "IHM";
                    break;
            }
        }
        return subjectName;
    }

    // API CALL
    public List<Request> getTopicRequests(Context context) {
        try {
            jsonResponce = Ion.with(context)
                    .load(BASE_URL + "/demande/sujet/" + String.valueOf(this.getIdTopic()))
                    .asJsonObject()
                    .get();
            Log.d("DEBUG", "jsonResponce: OK");
            Log.d("debug", jsonResponce.toString());

        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("DEBUG", "executionException");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("DEBUG", "InterruptionException");
        }

        topicRequests = new ArrayList<>();
        if (jsonResponce != null) {
            JsonArray jsonArrayRequests = jsonResponce.get("demande").getAsJsonArray();

            for (JsonElement jsonRequest : jsonArrayRequests) {

                int idStudent = jsonRequest.getAsJsonObject().get("EleveidEleve").getAsInt();
                String description = jsonRequest.getAsJsonObject().get("description").getAsString();
                String dateTime = jsonRequest.getAsJsonObject().get("dateheure").getAsString();
                String type = jsonRequest.getAsJsonObject().get("type").getAsString();
                Request request = new Request(this.idTopic, idStudent, description, dateTime, type);

                topicRequests.add(request);

            }
        }
        Log.d("debug", String.valueOf(this.idTopic));
        Log.d("debug", topicRequests.toString());
        return topicRequests;
    }
}
