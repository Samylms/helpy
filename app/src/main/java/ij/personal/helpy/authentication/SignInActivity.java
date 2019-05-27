package ij.personal.helpy.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ij.personal.helpy.Models.Class;
import ij.personal.helpy.R;

public class SignInActivity extends AppCompatActivity {

    private TextView txtAlreadyMember;
    private Button btnSignIn;
    private Class aClass;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        context = this;

        txtAlreadyMember = findViewById(R.id.txtAlreadyMember);
        txtAlreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // inscription
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aClass = new Class(1, 2019, "PRISM");
                aClass.addStudent(context);
            }
        });

    }
}
