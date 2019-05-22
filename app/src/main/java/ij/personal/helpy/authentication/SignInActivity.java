package ij.personal.helpy.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ij.personal.helpy.R;

public class SignInActivity extends AppCompatActivity {

    private TextView txtAlreadyMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtAlreadyMember = findViewById(R.id.txtAlreadyMember);
        txtAlreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
