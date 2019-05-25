package ij.personal.helpy.Need_Help;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ij.personal.helpy.R;

public class AddSubjectNeedHelpActivity extends AppCompatActivity {

    private EditText editSubjectName;
    private Spinner spinnerGeneralSubject;
    private Button btnValidate;
    private TextView txtCancel;

    private List<String> listGeneralSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject_need_help);

        editSubjectName = findViewById(R.id.edit_subject_name_need_help);
        spinnerGeneralSubject = findViewById(R.id.spinner_general_subject_need_help);
        btnValidate = findViewById(R.id.btn_validate_add_subject_need_help);
        txtCancel = findViewById(R.id.btn_cancel_add_subject_need_help);

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
}
