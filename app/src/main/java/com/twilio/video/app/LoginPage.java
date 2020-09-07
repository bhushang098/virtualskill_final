package com.twilio.video.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.SingleUserResponse.Data;
import com.twilio.video.app.SingleUserResponse.SingleUserResponse;
import com.twilio.video.app.util.NetworkOperator;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    Button loginButton,retryButton;
    TextView goReg,forgotpass;
    String email,password;
    EditText etmail,etpass;
    Dialog outhErrorDialog;
    ProgressBar progressBar;


    private static  String PREFS_NAME = "login_preferences";
    private static  String PREF_UNAME = "Username";
    private static  String PREF_PASSWORD = "Password";
    private  static  String TOKEN = "token";

    private  String DefaultUnameValue = "";
    private String UnameValue;

    private  String DefaultPasswordValue = "";
    private String PasswordValue;
    private Data userObj;
    private  String token;
    private String playerIdAsFcmToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        outhErrorDialog = new Dialog(this);
        userObj = new Data();
        setUi();
        Sprite foldingCube = new FoldingCube();
        foldingCube.setColor(Color.parseColor("#2073CC"));
        foldingCube.setScale(0.7f);
        progressBar.setIndeterminateDrawable(foldingCube);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etmail.getText().toString().trim();
                password = etpass.getText().toString().trim();
                if(email.isEmpty())
                {
                    etmail.setError("Email Cant Be Empty");
                }
                if(password.isEmpty()){
                    etpass.setError("Provide You Password");
                }
                if(!email.isEmpty() && !password.isEmpty())
                {

                    if(new NetworkOperator().checknetConnection(LoginPage.this))
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        loginButton.setVisibility(View.GONE);
                        loginByApi(email,password);


                    }else {
                        Toast.makeText(LoginPage.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginPage.this,ForgotPassPage.class));
            }
        });
        goReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginPage.this,RegistrationPage.class));
            }
        });

    }

    private void setOneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(LoginPage.this)
                .setNotificationReceivedHandler(new NotificationExtenderExample.ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationExtenderExample.ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                if (registrationId != null){
                    Log.d("OneSignalId", "registrationId:" + registrationId);
                    playerIdAsFcmToken = userId;
                    addFcm(playerIdAsFcmToken,token);
                }

            }
        });
    }


    private void loginByApi(String email, String password) {
        Call<Map> call = RetrifitClient.getInstance()
                .getApi().
                        loginUser(email,password,true);
        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                try{
                    if (response.body()==null)
                    {
                        if(response.errorBody()==null){
                            progressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                        }else {
                            showAuthError();
                        }

                    }else {
                        if(response.body().get("error")==null)
                        {
                             token = response.body().get("token").toString();
                            Log.d("HTML>>>>>  ", token);
                            setOneSignal();
                            //Saving uName And Password For Further Uses
                            //Get User Data

                        }else {
                            progressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                            showAuthError();
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    loginButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showAuthError();
            }
        });
    }



    private void getAndSaveUserData(String token) {

        Call<SingleUserResponse> call = RetrifitClient.getInstance()
                .getUserApi().getUSer(token);
        call.enqueue(new Callback<SingleUserResponse>() {
            @Override
            public void onResponse(Call<SingleUserResponse> call, Response<SingleUserResponse> response) {
                try{
                    if (response.body()==null)
                    {
                        if(response.errorBody()==null){
                            progressBar.setVisibility(View.GONE);
                        }else {
                            showAuthError();
                        }

                    }else {

                        userObj = response.body().getData();
                        savePreferences(token);


                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SingleUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showAuthError();
            }
        });
    }


        private void addFcm(String fcm_token, String token) {

            Call<MakeClassResponse> call = RetrifitClient.getInstance()
                    .getNotificationApi().addFcmToken(fcm_token,token);

            call.enqueue(new Callback<MakeClassResponse>() {
                @Override
                public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                    Log.d("Resposne>>",response.raw().toString());
                    if(response.body()!=null)
                    {
                        if(response.body().getStatus())
                        {
                            getAndSaveUserData(token);

                        }else {
                            progressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginPage.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Log.d("FcmAddFailude>>",t.toString());
                }
            });
        }


    private void setUi() {
        loginButton = findViewById(R.id.btnlogin);
        goReg = findViewById(R.id.tvRegister);
        forgotpass = findViewById(R.id.tvforgotPassword);
        etmail = findViewById(R.id.etMailLogin);
        etpass = findViewById(R.id.etPassLogin);
        progressBar = findViewById(R.id.progress_login);
    }

    private void savePreferences(String token) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();
        // Edit and commit
        UnameValue = email;
        PasswordValue = password;
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.putString(TOKEN,token);
        Gson gson = new Gson();
        String json = gson.toJson(userObj);
        editor.putString("UserObj", json);
        editor.commit();
        Intent i = new Intent(LoginPage.this,HomePage.class);
        i.putExtra("token",token);
        startActivity(i);
        progressBar.setVisibility(View.GONE);
        finish();
    }
    public void showAuthError() {
        progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        outhErrorDialog.setContentView(R.layout.auth_error_dialog);
        retryButton = outhErrorDialog.findViewById(R.id.btn_retry_login);
        outhErrorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.colorAccent));
        outhErrorDialog.show();

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outhErrorDialog.dismiss();
            }
        });

    }
}