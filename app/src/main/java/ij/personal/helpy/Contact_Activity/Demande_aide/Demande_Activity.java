package ij.personal.helpy.Contact_Activity.Demande_aide;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import ij.personal.helpy.R;

public class Demande_Activity extends AppCompatActivity {


    //for log
    private static final String Tag = "MainActivity";
    private ArrayList<String> mTextContact = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande);
        Log.d(Tag,"onCreate: started." );
        initName();

    }

    private void initName(){
        mTextContact.add("bernardo");
        mTextContact.add("alpacino");
        mTextContact.add("roberto");
        initReclyclerView();
    }

    private void  initReclyclerView(){
        Log.d(Tag, "initRecyclerView");
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        DemandeAdapter adapter = new DemandeAdapter(this, mTextContact);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}

