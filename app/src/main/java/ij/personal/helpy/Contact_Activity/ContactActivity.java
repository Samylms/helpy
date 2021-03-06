package ij.personal.helpy.Contact_Activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ij.personal.helpy.Models.Request;
import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;

public class ContactActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://54.37.157.172:3000";
    private String type;
    private String topicTitle;
    private int idTopic;
    private Context mContext;
    private List<Request> requestList;
    private RecyclerView requestRecyclerView;
    private RecyclerView.Adapter requestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;
    JsonObject jsonResponce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mContext = this;

        type = getIntent().getStringExtra("type");
        topicTitle = getIntent().getStringExtra("topicTitle");
        idTopic = getIntent().getIntExtra("idTopic", 0);

        // modify toolbar
        ActionBar toolbar = getSupportActionBar();
        if (type.equals("Proposition")) {
            toolbar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.green)));
        } else {
            toolbar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.red)));
        }
        setTitle(topicTitle);

        // get the data
        if (Prefs.isServerOK(mContext)) {
            requestList = getRequestList(mContext);
            // keep only corresponding request type
            for (int i=0; i<requestList.size(); i++) {
                if (requestList.get(i).getType().equals(this.type)){
                    requestList.remove(i);
                }
            }
            for (int i=0; i<requestList.size(); i++){
                if (requestList.get(i).getIdStudent() == Prefs.getStudentId(mContext)){
                    requestList.remove(i);
                }
            }
        } else {
            requestList = getFakeRequestList();
        }

        // Send data to the adapter to display
        if (requestList != null) {
            // handle recyclerView
            requestRecyclerView = findViewById(R.id.contactRecyclerView);
            requestRecyclerView.setHasFixedSize(true);
            // use a linear layout manager
            requestLayoutManager = new LinearLayoutManager(this);
            requestRecyclerView.setLayoutManager(requestLayoutManager);
            // specify an adapter
            requestAdapter = new ContactAdapter(requestList, this, type, topicTitle);
            requestRecyclerView.setAdapter(requestAdapter);
        } else {
            // todo: display "Aucun sujet en cours"
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<Request> getFakeRequestList() {
        List<Request> fakeRequests = new ArrayList<>();
        if (this.type.equals("Demande")) {
            fakeRequests.add(new Request(this.idTopic, 4, "blabla", "now", "Proposition"));
            fakeRequests.add(new Request(this.idTopic, 5, "blabla", "now", "Proposition"));
            fakeRequests.add(new Request(this.idTopic, 3, "blabla", "now", "Proposition"));
        }else {
            fakeRequests.add(new Request(this.idTopic, 6, "blabla", "now", "Demande"));
            fakeRequests.add(new Request(this.idTopic, 4, "blabla", "now", "Demande"));
            fakeRequests.add(new Request(this.idTopic, 3, "blabla", "now", "Demande"));
            fakeRequests.add(new Request(this.idTopic, 2, "blabla", "now", "Demande"));
        }
        return fakeRequests;
    }

    // API CALL
    public List<Request> getRequestList(Context context) {
        try {
            jsonResponce = Ion.with(context)
                    .load(BASE_URL + "/demande/sujet/" + String.valueOf(this.idTopic))
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

        List<Request> requests = new ArrayList<>();
        JsonArray jsonArrayRequests = jsonResponce.get("demande").getAsJsonArray();

        for (JsonElement jsonRequest : jsonArrayRequests) {

            int idStudent = jsonRequest.getAsJsonObject().get("EleveidEleve").getAsInt();
            String description = jsonRequest.getAsJsonObject().get("description").getAsString();
            String dateTime = jsonRequest.getAsJsonObject().get("dateheure").getAsString();
            String type = jsonRequest.getAsJsonObject().get("type").getAsString();
            Request request = new Request(this.idTopic, idStudent, description, dateTime, type);

            requests.add(request);

        }
        Log.d("debug", "request count :   " + requests.size());
        return requests;
    }
}
