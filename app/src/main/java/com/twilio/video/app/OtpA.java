package com.twilio.video.app;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.OTpResponse;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.SingleUserResponse.Data;
import com.twilio.video.app.SingleUserResponse.SingleUserResponse;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpA extends AppCompatActivity {

    EditText etOTP;
    String OTP,number,token,email,password;
    Button btnVerifyOTP,btnResendOTP;
    Dialog outhErrorDialog;
    TextView counter;
    int count = 60;
    ProgressBar progressBar;
    private Data userObj;
    private static  String PREFS_NAME = "login_preferences";
    private static  String PREF_UNAME = "Username";
    private static  String PREF_PASSWORD = "Password";
    private  static  String TOKEN = "token";

    private  String DefaultUnameValue = "";
    private String UnameValue;

    private  String DefaultPasswordValue = "";
    private String PasswordValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setUi();
        outhErrorDialog = new Dialog(this);
        number = getIntent().getStringExtra("number");
        token = getIntent().getStringExtra("token");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("pass");
        startTimer();


        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP = etOTP.getText().toString().trim();
                if(OTP.isEmpty())
                {
                    etOTP.setError("Enter OTP");
                }else {
                    if(OTP.length()==6)
                    {
                        verifyOTP();
                    }else {
                        Toast.makeText(OtpA.this, "OTP Must Bo of 6 Digits", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });
    }
    private void setUi() {
        etOTP = findViewById(R.id.etOtp);
        btnVerifyOTP = findViewById(R.id.btn_verify_otp);
        btnResendOTP = findViewById(R.id.btn_resend_otp);
        counter = findViewById(R.id.tv_counter_for_otp);
        progressBar = findViewById(R.id.progress_otp_verify);
    }

    public  void  startTimer(){
        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(count==0)
                        {
                            btnResendOTP.setVisibility(View.VISIBLE);
                            counter.setVisibility(View.INVISIBLE);
                            T.cancel();
                        }
                        counter.setText(" Otp Sent To Number "+number+
                                " \n Please Wait U will get OTP soon "+" \n U can Resend Otp in "+count+" sec");
                        count--;
                    }
                });
            }
        }, 1000, 1000);
    }

    private void verifyOTP() {
        progressBar.setVisibility(View.VISIBLE);
        Call<OTpResponse> call = RetrifitClient.getInstance()
                .getOTpApi().verifyOTP(OTP,token);
        call.enqueue(new Callback<OTpResponse>() {
            @Override
            public void onResponse(Call<OTpResponse> call, Response<OTpResponse> response) {
                try{
                    if (response.body()==null)
                    {
                        if(response.errorBody()==null){
                           progressBar.setVisibility(View.INVISIBLE);
                        }else {
                           showAuthError("Undefined Error");
                        }

                    }else {
                        if(response.body().getStatus()!=null)
                        {
                            if(response.body().getStatus())
                            {
                               shaveCredentials();
                            }

                        }else {
                           progressBar.setVisibility(View.INVISIBLE);
                           if(response.body().getStatus()==false)
                           showAuthError(response.body().getMessage());
                        }
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OTpResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                showAuthError("Undefined Error");
            }
        });
    }

    private void shaveCredentials() {

        Call<SingleUserResponse> call = RetrifitClient.getInstance()
                .getUserApi().getUSer(token);
        call.enqueue(new Callback<SingleUserResponse>() {
            @Override
            public void onResponse(Call<SingleUserResponse> call, Response<SingleUserResponse> response) {
                try{
                    if (response.body()==null)
                    {
                        if(response.errorBody()==null){
                            progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            showAuthError(response.raw().toString());
                        }

                    }else {
                       userObj = response.body().getData();
                       ///delete Preference And Go to Login Page For Fcm Toekn Amd All

                        deletePreferences(token);
                        Intent i = new Intent(OtpA.this,LoginPage.class);
                        Toast.makeText(OtpA.this, "Registration SuccessFull", Toast.LENGTH_SHORT).show();
                        i.putExtra("token",token);
                        startActivity(i);
                        finish();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SingleUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                showAuthError(t.toString());
            }
        });

    }

    private void resendOtp() {
        progressBar.setVisibility(View.VISIBLE);
        Call<OTpResponse> call = RetrifitClient.getInstance()
                .getOTpApi().verifyOTP(OTP,token);
        call.enqueue(new Callback<OTpResponse>() {
            @Override
            public void onResponse(Call<OTpResponse> call, Response<OTpResponse> response) {
                try{
                    if (response.body()==null)
                    {
                        if(response.errorBody()==null){
                            progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            showAuthError("Undefined Error");
                        }

                    }else {
                        if(response.body().getStatus()!=null)
                        {
                            if(response.body().getStatus())
                            {
                                Toast.makeText(OtpA.this, "OTP  Sent ", Toast.LENGTH_LONG).show();
                            }

                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            if(response.body().getStatus()==false)
                                showAuthError(response.body().getMessage());
                        }
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OTpResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                showAuthError("Undefined Error");
            }
        });
    }

    private void deletePreferences(String token) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public void showAuthError(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        outhErrorDialog.setContentView(R.layout.reg_error_dialog);
        //retryButton = outhErrorDialog.findViewById(R.id.btn_retry_login);
        outhErrorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.colorAccent));
        outhErrorDialog.show();
        Button retryButton = outhErrorDialog.findViewById(R.id.btn_retry_register);
        TextView tvError = outhErrorDialog.findViewById(R.id.tv_regError_desc);

        tvError.setText(error);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outhErrorDialog.dismiss();
            }
        });

    }
}