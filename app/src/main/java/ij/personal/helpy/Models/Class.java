package ij.personal.helpy.Models;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

// CLASSES
public class Class {

    int id;
    int year;
    String name;
    List<Topic> classTopics;

    // to handle the json
    private JsonObject jsonResponce;

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

    public List<Topic> getClassTopics (Context context){

        // La library Ion communique avec l'API.
        // ici, la méthode get() oblige l'attente du retour et donc de la création du fichier JSON
        try {
            jsonResponce = Ion.with(context)
                    .load("http://185.225.210.63:3000/sujet/classe/" + String.valueOf(this.getId()))
                    .asJsonObject()
                    .get();
            Log.d("DEBUG", "jsonResponce: OK");

            classTopics = new ArrayList<>();
            JsonArray jsonArrayTopics = jsonResponce.get("sujet").getAsJsonArray();

            for (JsonElement jsonTopic: jsonArrayTopics) {

                int idTopic = jsonTopic.getAsJsonObject().get("idSujet").getAsInt();
                String title = jsonTopic.getAsJsonObject().get("titre").getAsString();
                int idSubject = jsonTopic.getAsJsonObject().get("idMatiere").getAsInt();
                Topic topic = new Topic(idTopic, title, this.id, idSubject);

                classTopics.add(topic);

            }
            Log.d("debug" , String.valueOf(this.id));
            Log.d("debug", classTopics.toString());
            return classTopics;

        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("DEBUG", "executionException");
            Log.d("DEBUG", e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("DEBUG", "InterruptionException");
            Log.d("DEBUG", e.toString());
        }
        return null;

    }

    //        Ion.with(context)
////                .load("http://api.icndb.com/jokes/random")
//                .load("http://185.225.210.63:3000/sujet/classe/" + String.valueOf(this.getId()))
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        joke = result.get("value").getAsJsonObject().get("joke").getAsString();
//                        // set the joke on the textView
//                        TextView txtJoke = findViewById(R.id.txtJoke);
//                        txtJoke.setText(joke);
//                        // manage the string
//                        joke = joke.replace('.', ' ');
//                        joke = joke.replace(' ', '_');
//                        jokeURL = "https://memegen.link/iw/" + joke + ".jpg";
//                        imgJoke = findViewById(R.id.imgJoke);
//                        /*
//                        Download the logo from online and set it as
//                        ImageView image programmatically.
//                        */
//                        new DownLoadImageTask(imgJoke).execute(jokeURL);
//                    }
//                });

    public void addStudent(Context context){
        JsonObject json = new JsonObject();
//        json.addProperty("foo", "bar");
        json.addProperty("classeId", this.getId());
        json.addProperty("mail", "jerome.isoard@u-psud.fr");
        json.addProperty("mdp", "prism2019");
        json.addProperty("nom", "isoard");
        json.addProperty("prenom", "jerome");
        json.addProperty("telephone", 0617534110);

        Log.d("debug", json.toString());

        Ion.with(context)
                .load("http://185.225.210.63:3000/eleve/inscription")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
//                        Log.d("DEBUG", e.toString());
                        Log.d("debug", String.valueOf(result.getAsJsonObject().get("code").getAsInt()));
                    }
                });
        Log.d("debug", "************** student sign In");
    }
}
