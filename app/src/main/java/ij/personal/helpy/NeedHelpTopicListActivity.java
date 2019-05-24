package ij.personal.helpy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import ij.personal.helpy.Models.Class;
import ij.personal.helpy.Models.Topic;

public class NeedHelpTopicListActivity extends AppCompatActivity {

    private RecyclerView topicRecyclerView;
    private RecyclerView.Adapter topicAdapter;
    private RecyclerView.LayoutManager topicLayoutManager;

    private Class studentClass;
    private List<Topic> topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help_topic_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabAddSubject = findViewById(R.id.fab_add_subject);
        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), AddTopicActivity.class);
                startActivity(intent);
            }
        });

        // Pour la flèche de retour
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // GET ALL DATA
        studentClass = new Class(1, 2019, "Prism");
        topics = studentClass.getClassTopics(this);

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

    // Pour revenir à l'activité précédente via la flèche de retour
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
