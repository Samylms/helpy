package ij.personal.helpy.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import ij.personal.helpy.MainActivity;
import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;

public class SignInActivity extends AppCompatActivity {

    private EditText txtName;
    private EditText txtMail;
    private EditText txtPwd;
    private EditText txtPromotion;
    private TextView txtAlreadyMember;
    private Button btnSignIn;
    private Context mContext;
    String inputMail;
    String inputPwd;
    String inputName;
    String inputPromotion;
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        settings = getSharedPreferences("UserInfo", 0);
        editor = settings.edit();

        mContext = this;
        txtName = findViewById(R.id.txtName);
        txtMail = findViewById(R.id.txtMail);
        txtPwd = findViewById(R.id.txtPwd);
        txtPromotion = findViewById(R.id.txtPromotion);

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
                if (Prefs.isServerOK(mContext)) {
                    inputMail = txtMail.getText().toString();
                    inputPwd = txtPwd.getText().toString();
                    inputName = txtName.getText().toString();
                    inputPromotion = txtPromotion.getText().toString();
                    if (inputMail.equals("") || inputPwd.equals("") || inputName.equals("") || inputPromotion.equals("")) {
                        Toast.makeText(mContext, "Veuillez saisir tous les champs.", Toast.LENGTH_SHORT).show();
                    } else {
                        registerStudent(mContext, inputName, inputMail, inputPwd, inputPromotion);
                        editor.putBoolean("isStudentConnected", true);
                        editor.apply();
                        LoginActivity.loginActivity.finish();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    }
                }else{
                    editor.putBoolean("isStudentConnected", true);
                    editor.apply();
                    LoginActivity.loginActivity.finish();
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

    }

    // API CALL
    public void registerStudent(Context context, String lastName, String mail, String pwd, String className) {
        JsonObject json = new JsonObject();
        json.addProperty("nom", lastName);
        json.addProperty("mail", mail);
        json.addProperty("mdp", pwd);
        json.addProperty("classeLibelle", className);

        Log.d("debug", json.toString());

        Ion.with(context)
                .load("http://185.225.210.63:3000/eleve/inscription")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d("DEBUG", e.toString());
                            Toast.makeText(mContext, getString(R.string.toast_signin_error), Toast.LENGTH_LONG).show();
                        }
                        if (result != null) {
                            Log.d("debug", result.getAsJsonObject().toString());
                            int code = result.getAsJsonObject().get("code").getAsInt();
                            if (code == 2) {
                                Log.d("debug", "classe inexistante");
                                Toast.makeText(mContext, getString(R.string.toast_signin_invalid_class), Toast.LENGTH_LONG).show();
                            } else if (code == 0) {
                                Log.d("debug", "Mail existe deja");
                                Toast.makeText(mContext, getString(R.string.toast_signin_invalid_mail), Toast.LENGTH_LONG).show();
                            } else if (code == 1) {
                                Log.d("debug", "Sign In Success");
                                int idClass = result.getAsJsonObject().get("idClasse").getAsInt();
                                int idEleve = result.getAsJsonObject().get("idEleve").getAsInt();
                                handleSignInSuccess(idClass, idEleve);
                            }
                        }
                    }
                });
        Log.d("debug", "************** student sign In");
    }

    public void handleSignInSuccess(int idClasse, int idEleve) {
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("studentId", idEleve);
        editor.putInt("classId", idClasse);
        editor.putString("studentLastName", inputName);
        editor.putString("studentEmail", inputMail);
        editor.putString("studentPwd", inputPwd);
        editor.putString("className", inputPromotion);
        editor.putBoolean("isStudentConnected",true);
        editor.apply();

        // navigate to main activity
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }
}
