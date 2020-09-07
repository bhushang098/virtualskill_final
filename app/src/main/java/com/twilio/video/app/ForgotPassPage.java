package com.twilio.video.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.twilio.video.app.ApiModals.MakeClassResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassPage extends AppCompatActivity {

    EditText etphone,etotp,etPass1,etpass2;
    Button getOypBtn,btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_page);
        setUI();

        getOypBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etphone.getText().toString().length()<10)
                {
                    etphone.setError("Invalid Phone Number");
                }else {
                    getOtp(etphone.getText().toString());
                }
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etotp.getText().toString().isEmpty()||
                        etPass1.getText().toString().isEmpty()||
                        etpass2.getText().toString().isEmpty() ){
                    Toast.makeText(ForgotPassPage.this, "Provide All Fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (etPass1.getText().toString().equals(etpass2.getText().toString()))
                    {
                        changePassByOtp();
                    }else {
                        Toast.makeText(ForgotPassPage.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void changePassByOtp() {
        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().changePassByOtp(etotp.getText().toString(),etphone.getText().toString(),
                        etPass1.getText().toString(),etpass2.getText().toString());

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("Response >>", response.raw().toString());

                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        Toast.makeText(ForgotPassPage.this, "Changed Successfully ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassPage.this, LoginPage.class));
                        finish();
                    }else {
                        Toast.makeText(ForgotPassPage.this, "Error Changing Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {

            }
        });
    }

    private void getOtp(String toString) {
        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().getOTp(toString);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d(">>Response>>",response.raw().toString());
                if (response.body()!=null)
                {
                    if (response.body().getStatus())
                    {
                        Toast.makeText(ForgotPassPage.this, "Otp Sent To Your Number", Toast.LENGTH_LONG).show();
                        etphone.setVisibility(View.GONE);
                        getOypBtn.setVisibility(View.GONE);

                        etotp.setVisibility(View.VISIBLE);
                        etPass1.setVisibility(View.VISIBLE);
                        etpass2.setVisibility(View.VISIBLE);
                        btnChangePass.setVisibility(View.VISIBLE);


                    }else {
                        Toast.makeText(ForgotPassPage.this, "Can Not Send OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {

            }
        });
    }

    private void setUI() {
        getOypBtn = findViewById(R.id.btn_get_otp_on_forgot_pass);
        etphone = findViewById(R.id.et_phone_forgot_pass);
        etotp = findViewById(R.id.et_otp_forgot_pass);
        etPass1 = findViewById(R.id.et_pass1_forgot_pass);
        etpass2 = findViewById(R.id.et_pass2_forgot_pass);
        btnChangePass = findViewById(R.id.btn_change_pass_on_forgot_pass);
    }
}