package ij.personal.helpy.Need_Help;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ij.personal.helpy.Models.Class;
import ij.personal.helpy.Models.Topic;
import ij.personal.helpy.R;
import ij.personal.helpy.TopicAdapter;

public class TopicListNeedHelpActivity extends AppCompatActivity {

    private RecyclerView topicRecyclerView;
    private RecyclerView.Adapter topicAdapter;
    private RecyclerView.LayoutManager topicLayoutManager;

    private Class studentClass;
    private List<Topic> topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list_need_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabAddSubject = findViewById(R.id.fab_add_subject);
        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), AddSubjectNeedHelpActivity.class);
                startActivity(intent);
            }
        });

        // Pour la flèche de retour
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // GET ALL DATA
        studentClass = new Class(1, 2019, "Prism");
//        topics = studentClass.getClassTopics(this);

        // if server is killed (only for test)
        if (topics == null){
            topics = new ArrayList<>();
            topics = new ArrayList<>();
            topics.add(new Topic(0, "Lab 3" , 1, 1));
            topics.add(new Topic(0, "Lab 2" , 1, 1));
            topics.add(new Topic(0, "Projet" , 1, 2));
            topics.add(new Topic(0, "Revision" , 1, 3));
            topics.add(new Topic(0, "TP" , 1, 2));
        }

        // Send data to the adapter to display
        if(topics != null){
            // handle recyclerView
            topicRecyclerView = findViewById(R.id.topicRecyclerView);
            topicRecyclerView.setHasFixedSize(true);
            // use a linear layout manager
            topicLayoutManager = new LinearLayoutManager(this);
            topicRecyclerView.setLayoutManager(topicLayoutManager);
            // specify an adapter
            topicAdapter = new TopicAdapter(topics, this);
            topicRecyclerView.setAdapter(topicAdapter);
        }
    }

    // Pour revenir à l'activité précédente via la flèche de retour
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
