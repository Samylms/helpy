package ij.personal.helpy.TopicList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ij.personal.helpy.Models.Topic;
import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;

public class TopicListActivity extends AppCompatActivity {

    private RecyclerView topicRecyclerView;
    private RecyclerView.Adapter topicAdapter;
    private RecyclerView.LayoutManager topicLayoutManager;
    private Context mContext;
    private String type;
    public static boolean proposition;

    private List<Topic> topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // to know if its an need or a can
        type = getIntent().getStringExtra("type");
        if (type.equals("proposition")){
            proposition = true;
        }else{
            proposition = false;
        }

        mContext = this;

        setContentView(R.layout.activity_topic_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(proposition) {
            toolbar.setBackgroundColor(getColor(R.color.green));
            toolbar.setTitle(getString(R.string.title_can_help));
        }

        FloatingActionButton fabAddSubject = findViewById(R.id.fab_add_subject);
        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTopicActivity.class);
                startActivity(intent);
            }
        });

        // Pour la flèche de retour
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // GET ALL DATA
        if (Prefs.isServerOK(mContext)) {
            topics = getTopicList(this, Prefs.getClassId(mContext));
        } else {
            // server is killed (only for test)
            topics = new ArrayList<>();
            if (proposition){
                topics.add(new Topic(0, "Lab 2", 1, 1));
                topics.add(new Topic(0, "TP", 1, 2));
            }else {
                topics.add(new Topic(0, "Lab 3", 1, 1));
                topics.add(new Topic(0, "Lab 2", 1, 1));
                topics.add(new Topic(0, "Projet", 1, 2));
                topics.add(new Topic(0, "Revision", 1, 3));
                topics.add(new Topic(0, "TP", 1, 2));
            }
        }

        // Send data to the adapter to display
        if (topics != null) {
            // handle recyclerView
            topicRecyclerView = findViewById(R.id.topicRecyclerView);
            topicRecyclerView.setHasFixedSize(true);
            // use a linear layout manager
            topicLayoutManager = new LinearLayoutManager(this);
            topicRecyclerView.setLayoutManager(topicLayoutManager);
            // specify an adapter
            topicAdapter = new TopicAdapter(topics, this);
            topicRecyclerView.setAdapter(topicAdapter);
        }else{
            // todo: display "Aucun sujet en cours"
        }
    }

    // Pour revenir à l'activité précédente via la flèche de retour
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // API CALL
    public List<Topic> getTopicList(Context context, int idClass) {
        JsonObject jsonResponce;
        try {
            jsonResponce = Ion.with(context)
                    .load("http://185.225.210.63:3000/sujet/classe/" + idClass)
                    .asJsonObject()
                    .get();
            Log.d("DEBUG", "jsonResponce: OK");

            List<Topic> topicList = new ArrayList<>();
            JsonArray jsonArrayTopics = jsonResponce.get("sujet").getAsJsonArray();

            for (JsonElement jsonTopic : jsonArrayTopics) {
                int idTopic = jsonTopic.getAsJsonObject().get("idSujet").getAsInt();
                String title = jsonTopic.getAsJsonObject().get("titre").getAsString();
                int idSubject = jsonTopic.getAsJsonObject().get("idMatiere").getAsInt();
                Topic topic = new Topic(idTopic, title, idClass, idSubject);
                topicList.add(topic);
            }

            Log.d("debug", String.valueOf(idClass));
            Log.d("debug", topicList.toString());
            return topicList;

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

}
