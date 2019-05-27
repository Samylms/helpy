package ij.personal.helpy.Contact_Activity.Propose_aide;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import ij.personal.helpy.R;

public class Propose_Activity extends AppCompatActivity {

    //for log
    private static final String Tag = "MainActivity";
    private ArrayList<String> mTextPropose = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose);
        Log.d(Tag,"onCreate: started." );
        initName();

    }

    private void initName(){
        mTextPropose.add("bernardo");
        mTextPropose.add("alpacino");
        mTextPropose.add("roberto");
        initReclyclerView();
    }

    private void  initReclyclerView(){
        Log.d(Tag, "initRecyclerView");
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view_propose);
        ProposeAdapter adapter = new ProposeAdapter(this, mTextPropose);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
