package ij.personal.helpy;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ij.personal.helpy.TopicList.AddTopicActivity;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    CheckBox chkContactPhone, chkContactSms, chkContactMail, chkContactAlertP, chkContactAlertG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // modify toolbar
        ActionBar toolbar = getSupportActionBar();
        toolbar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.darkGrey)));
        setTitle("Editer mon Profil");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        chkContactPhone = findViewById(R.id.chk_contact_phone);
        chkContactSms = findViewById(R.id.chk_contact_sms);
        chkContactMail = findViewById(R.id.chk_contact_mail);
        chkContactAlertP = findViewById(R.id.chk_contact_alert_p);
        chkContactAlertG = findViewById(R.id.chk_contact_alert_g);

        chkContactPhone.setOnCheckedChangeListener(this);
        chkContactSms.setOnCheckedChangeListener(this);
        chkContactMail.setOnCheckedChangeListener(this);
        chkContactAlertP.setOnCheckedChangeListener(this);
        chkContactAlertG.setOnCheckedChangeListener(this);

        // handle FAB validate
//        FloatingActionButton fabAddSubject = findViewById(R.id.fab_add_subject);
//        fabAddSubject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // todo : API Call update Student
//                finish();
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setButtonTintList(getColorStateList(R.color.darkGrey));
            buttonView.setTextColor(getColor(R.color.darkGrey));
        }else{
            buttonView.setButtonTintList(getColorStateList(R.color.grey));
            buttonView.setTextColor(getColor(R.color.grey));
        }
    }
}
