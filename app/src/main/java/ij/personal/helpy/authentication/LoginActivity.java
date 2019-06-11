package ij.personal.helpy.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

import ij.personal.helpy.MainActivity;
import ij.personal.helpy.Models.Student;
import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;

public class LoginActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://54.37.157.172:3000";
//    public static final String BASE_URL = "http://185.225.210.63:3000";
    private TextView txtCreateAccount;
    private Button btnConnect;
    private EditText txtInputMail;
    private EditText txtInputPwd;
    private Context mContext;
    public static LoginActivity loginActivity;
    String inputMail;
    String inputPwd;
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        loginActivity = this; //to be able to finish this activity from SignInActivity
        settings = getSharedPreferences("UserInfo", 0);
        editor = settings.edit();

        // ----------------------------------------
        // ------------ SERVER STATE --------------
        // ----------------------------------------
        editor.putBoolean("isServerOK", true);
        editor.commit();
        Log.d("debug", "isServerOK = "  + String.valueOf(Prefs.isServerOK(mContext)));
        // ----------------------------------------
        // ----------------------------------------

        // if user has already logged
        if (Prefs.isStudentConnected(mContext)){
            // navigate to main activity
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }

        mContext = this;
        txtInputMail = findViewById(R.id.txtInputMail);
        txtInputPwd = findViewById(R.id.txtInputPwd);

        txtCreateAccount = findViewById(R.id.btnCreateAccount);
        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignInActivity.class));
            }
        });

        btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefs.isServerOK(mContext)){
                    inputMail = txtInputMail.getText().toString();
                    inputPwd = txtInputPwd.getText().toString();
                    if (inputMail.equals("") || inputPwd.equals("")) {
                        Toast.makeText(mContext, "Veuillez saisir une adresse mail et un mot de passe.", Toast.LENGTH_SHORT).show();
                    } else {
                        logStudent(mContext, inputMail, inputPwd);
                        editor.putBoolean("isStudentConnected", true);
                        editor.apply();
                    }
                }else{
                    editor.putBoolean("isStudentConnected", true);
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    // API CALL
    public void logStudent(Context context, String mail, String pwd){
        JsonObject json = new JsonObject();
        json.addProperty("mail", mail);
        json.addProperty("mdp", pwd);

        Log.d("debug", json.toString());

        Ion.with(context)
                .load( BASE_URL + "/eleve/login")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d("DEBUG", e.toString());
                            Toast.makeText(mContext, getString(R.string.toast_login_error), Toast.LENGTH_LONG).show();

                        }
                        if (result != null) {
                            Log.d("debug", result.getAsJsonObject().toString());
                            if(result.getAsJsonObject().get("code").getAsInt() == 0){
                                Log.d("debug", "Aucune correspondance");
                                Toast.makeText(mContext, getString(R.string.toast_login_invalid), Toast.LENGTH_LONG).show();
                            }else{
                                int idEleve = result.getAsJsonObject().get("idEleve").getAsInt();
                                int idClasse = result.getAsJsonObject().get("classeId").getAsInt();
                                handleLoginSuccess(idClasse, idEleve);
                                Log.d("debug", "************** student Log In");
                                displayUserInfos();
                            }
                        }
                    }
                });
    }

    public void handleLoginSuccess(int idClass, int idStudent){
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("studentId", idStudent);
        editor.putInt("classId", idClass);
        editor.putString("studentEmail", inputMail);
        editor.putString("studentPwd", inputPwd);

        // get user info from API
        Student user = getStudent(idStudent);
        if(user != null) {
            editor.putString("studentLastName", user.getLastName());
            editor.putString("studentFirstName", user.getFirstName());
            editor.putInt("studentPhone", user.getPhone());
            editor.putInt("studentPrefPhone", user.getPrefPhone());
            editor.putInt("studentPrefSms", user.getPrefSms());
            editor.putInt("studentPrefMail", user.getPrefMail());
            editor.putInt("studentPrefAlertP", user.getPrefAlertP());
            editor.putInt("studentPrefAlertG", user.getPrefAlertG());
        }

        // get className from API
        String className = getClassname(idClass);
        if (className != null) {
            editor.putString("className", className);
        }

        // put the user connected
        editor.putBoolean("isUserConnected",true);
        editor.apply();

        // navigate to main activity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public Student getStudent(int idStudent){
        Log.d("debug", "idStudent" + idStudent);
        try {
            JsonObject jsonResponce = Ion.with(mContext)
                    .load(BASE_URL + "/eleve/" + String.valueOf(idStudent))
                    .asJsonObject()
                    .get();
            Log.d("DEBUG", jsonResponce.toString());

            Student student = new Student(idStudent, inputMail, inputPwd, "", "", 0,
                    0, 0,0,0,0,0);
            student.setLastName(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("nom").getAsString());
            student.setFirstName(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prenom").getAsString());
            student.setPhone(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("telephone").getAsInt());
            student.setPrefPhone(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefAppel").getAsInt());
            student.setPrefSms(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefMessage").getAsInt());
            student.setPrefMail(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefMail").getAsInt());
            student.setPrefAlertP(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefNotifPersonelles").getAsInt());
            student.setPrefAlertG(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefNotifGlobales").getAsInt());
            student.setIdClass(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("ClasseidClasse").getAsInt());


            Log.d("debug", student.toString());
            return student;

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

    public String getClassname(int idClass){
        try {
            JsonObject jsonResponce = Ion.with(mContext)
                    .load(BASE_URL + "/classe/" + String.valueOf(idClass))
                    .asJsonObject()
                    .get();
            Log.d("DEBUG", "jsonResponce: OK");
            Log.d("debug", jsonResponce.toString());
            return jsonResponce.get("classe").getAsJsonArray().get(0).getAsJsonObject().get("libelle").getAsString();

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

    public void displayUserInfos(){
        Log.d("debug", "classId : " + String.valueOf(Prefs.getClassId(mContext)));
        Log.d("debug", "className : " + Prefs.getClassName(mContext));
        Log.d("debug", "StudentId : " + String.valueOf(Prefs.getStudentId(mContext)));
        Log.d("debug", "StudentMail : " + Prefs.getStudentEmail(mContext));
        Log.d("debug", "studentPwd : " + Prefs.getStudentPwd(mContext));
        Log.d("debug", "studentFistName : " + Prefs.getStudentFirstName(mContext));
        Log.d("debug", "studentLastName : " + Prefs.getStudentLastName(mContext));
        Log.d("debug", "studentPhone : " + String.valueOf(Prefs.getStudentPhone(mContext)));
        Log.d("debug", "prefPhone : " + String.valueOf(Prefs.getStudentPrefPhone(mContext)));
        Log.d("debug", "prefSms : " + String.valueOf(Prefs.getStudentPrefSms(mContext)));
        Log.d("debug", "prefMail : " + String.valueOf(Prefs.getStudentPrefMail(mContext)));
        Log.d("debug", "prefAlertP : " + String.valueOf(Prefs.getStudentPrefAlertP(mContext)));
        Log.d("debug", "prefAlertG : " + String.valueOf(Prefs.getStudentPrefAlertG(mContext)));
    }
}
