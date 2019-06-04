package ij.personal.helpy.TopicList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ij.personal.helpy.MainActivity;
import ij.personal.helpy.R;

public class AddTopicActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editSubjectName;
    private Spinner spinnerGeneralSubject;
    private Button btnValidate;
    private TextView txtInfoDemand, txtCancel;

    private List<String> listGeneralSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        editSubjectName = findViewById(R.id.edit_subject_name);
        spinnerGeneralSubject = findViewById(R.id.spinner_general_subject);
        txtInfoDemand = findViewById(R.id.txt_info_demand);
        btnValidate = findViewById(R.id.btn_validate_add_subject);
        txtCancel = findViewById(R.id.btn_cancel_add_subject);

        if(TopicListActivity.proposition){
            txtInfoDemand.setText(getText(R.string.text_inscription_can_helper));
            btnValidate.setBackgroundColor(getColor(R.color.green));
            txtCancel.setTextColor(getColor(R.color.green));
        }

        listGeneralSubject = new ArrayList<>();

        //On test si on reçoit bien les bonnes data, si non, on met des matières par défaut
        if(!listGeneralSubject.isEmpty()){
            // on rempli la liste du spinner dynamiquement

        }else {
            // Valeurs par défaut
            listGeneralSubject.add("Web");
            listGeneralSubject.add("Android");
            listGeneralSubject.add("iOS");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listGeneralSubject);
        spinnerGeneralSubject.setAdapter(dataAdapter);
        spinnerGeneralSubject.setOnItemSelectedListener(this);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter le sujet dans la base de données puis afficher le contenu modifié

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
        String item = parent.getItemAtPosition(position).toString();
        System.out.println("################################### item = " + item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
