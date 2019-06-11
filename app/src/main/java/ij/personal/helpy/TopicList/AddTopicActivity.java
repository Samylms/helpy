package ij.personal.helpy.TopicList;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import ij.personal.helpy.MainActivity;
import ij.personal.helpy.Models.Subject;
import ij.personal.helpy.Models.Topic;
import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;

public class AddTopicActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String BASE_URL = "http://54.37.157.172:3000";
    private EditText editSubjectName;
    private Spinner spinnerGeneralSubject;
    private Button btnValidate;
    private TextView txtInfoDemand, txtCancel;
    private int selectedSujectId;

    private List<StringSubject> listGeneralSubject;
    private List<Subject> subjectList;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        mContext = this;

        if(Prefs.isServerOK(mContext)) {
            subjectList = getClassSubjects(Prefs.getClassId(mContext));
        }else{
            subjectList = new ArrayList<>();
            subjectList.add(new Subject(6, "Web"));
            subjectList.add(new Subject(7, "Android"));
            subjectList.add(new Subject(8, "IOS"));
            subjectList.add(new Subject(9, "IHM"));
        }
        editSubjectName = findViewById(R.id.edit_subject_name);
        spinnerGeneralSubject = findViewById(R.id.spinner_general_subject);
        txtInfoDemand = findViewById(R.id.txt_info_demand);
        btnValidate = findViewById(R.id.btn_validate_add_subject);
        txtCancel = findViewById(R.id.btn_cancel_add_subject);

        if(TopicListActivity.proposition){
            txtInfoDemand.setText(getText(R.string.text_inscription_can_helper));
            btnValidate.setBackground(getDrawable(R.drawable.button_green));
            txtCancel.setTextColor(getColor(R.color.green));
        }

        listGeneralSubject = new ArrayList<>();
        for(Subject subject: subjectList){
            listGeneralSubject.add(new StringSubject(String.valueOf(subject.getIdSubject()), subject.getName()));
        }

        ArrayAdapter<StringSubject> dataAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listGeneralSubject);
        spinnerGeneralSubject.setAdapter(dataAdapter);
        spinnerGeneralSubject.setOnItemSelectedListener(this);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter le sujet dans la base de données puis afficher le contenu modifié
                //todo: add topic API CALL
                addTopic(editSubjectName.getText().toString(), selectedSujectId, Prefs.getClassId(mContext));
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        StringSubject item = (StringSubject) parent.getSelectedItem();
        Log.d("debug", "id : " + item.getId() + "   name: " + item.getName());
        selectedSujectId = Integer.parseInt(item.getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // API CALL
    public List<Subject> getClassSubjects(int idClass){
        JsonObject jsonResponce;
        try {
            jsonResponce = Ion.with(mContext)
                    .load(BASE_URL + "/matiere/classe/" + idClass)
                    .asJsonObject()
                    .get();
            Log.d("DEBUG", jsonResponce.toString());

            List<Subject> subjectList = new ArrayList<>();
            JsonArray jsonArraySubjects = jsonResponce.get("matiere").getAsJsonArray();

            for (JsonElement jsonSubject : jsonArraySubjects) {
                int idSubject = jsonSubject.getAsJsonObject().get("idMatiere").getAsInt();
                String libelle = jsonSubject.getAsJsonObject().get("libelle").getAsString();
                Subject subject = new Subject(idSubject, libelle);
                subjectList.add(subject);
            }

            Log.d("debug", String.valueOf(idClass));
            Log.d("debug", subjectList.toString());
            return subjectList;

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

    // API CALL
    public void addTopic(String topicName, int idSubject, int idClass){
        JsonObject json = new JsonObject();
        json.addProperty("titre", topicName);
        json.addProperty("idMatiere", idSubject);
        json.addProperty("classeId", idClass);

        Log.d("debug", json.toString());

        Ion.with(mContext)
                .load(BASE_URL + "/sujet")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d("DEBUG", e.toString());
                            Toast.makeText(mContext, getString(R.string.toast_adding_topic_error), Toast.LENGTH_LONG).show();
                        }
                        if (result != null) {
                            Log.d("debug", result.getAsJsonObject().toString());
                            int code = result.getAsJsonObject().get("code").getAsInt();
                            if (code == 0) {
                                Log.d("debug", "Add topic failed");
                            } else if (code == 1) {
                                Log.d("debug", "Add topic Success");
                                finish();
                            }
                        }
                    }
                });
    }

    public class StringSubject {

        private String id;
        private String name;

        public StringSubject(String id, String name) {
            this.id = id;
            this.name = name;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        //to display object as a string in spinner
        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof StringSubject){
                StringSubject s = (StringSubject )obj;
                if(s.getName().equals(name) && s.getId()==id ) return true;
            }

            return false;
        }

    }
}
