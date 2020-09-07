package com.twilio.video.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.util.NetworkOperator;

public class WelcomeScreen extends AppCompatActivity {

    Button btnWecome;
    private static String PREFS_NAME = "login_preferences";
    private static String PREF_UNAME = "Username";
    private static String PREF_PASSWORD = "Password";
    private static String TOKEN = "token";
    private String DefaultUnameValue = "";
    private String DefaultTokenValue = "";
    private String UnameValue;
    private String tokenValue;

    private String DefaultPasswordValue = "";
    private String PasswordValue;
    String fcm_token;
    NetworkOperator networkOperator;
    UserObj userObj;
    boolean firsStart;


    @Override
    protected void onStart() {
        super.onStart();
        networkOperator = new NetworkOperator();
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
         firsStart = sharedPreferences.getBoolean("firstStart", true);
        if (firsStart) {
            // First Time Loaded

        } else {
            loadPreferences();
            if (tokenValue.length() > 0) {
                    Intent i = new Intent(WelcomeScreen.this, HomePage.class);
                    i.putExtra("token", tokenValue);
                    startActivity(i);
                    finish();
            } else {
                startActivity(new Intent(WelcomeScreen.this, LoginPage.class));
                finish();
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        setUi();


        btnWecome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startbgService();
                if (networkOperator.checknetConnection(WelcomeScreen.this)&& firsStart) {
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("FCM_TOKEN", "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    // Get new Instance ID token
                                    fcm_token = task.getResult().getToken();
                                    SharedPreferences sharedPreferences1 = getSharedPreferences("prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                                    editor.putBoolean("firstStart", false);
                                    editor.putString("fcm_token", fcm_token);
                                    editor.apply();
                                    startActivity(new Intent(WelcomeScreen.this, LoginPage.class));
                                    finish();

                                    // Log and toast
                                    //String msg = getString(R.string.msg_token_fmt, token);
                                    Log.d("FCM_TOKEN!!!", fcm_token);
                                    //Toast.makeText(WelcomeScreen.this, fcm_token, Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(WelcomeScreen.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void startbgService() {
        startService(new Intent(WelcomeScreen.this,MyNotificationService.class));
    }


    private void setUi() {
        btnWecome = findViewById(R.id.btn_welcome);
    }

    private UserObj loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        tokenValue = settings.getString(TOKEN, DefaultTokenValue);
        Gson gson = new Gson();
        String json = settings.getString("UserObj", "");
        UserObj obj = gson.fromJson(json, UserObj.class);

        return obj;

    }

}