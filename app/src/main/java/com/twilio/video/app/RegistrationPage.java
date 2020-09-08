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
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.twilio.video.app.ApiModals.RegResponse;
import com.twilio.video.app.util.NetworkOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationPage extends AppCompatActivity {
    TextView tvGologin;
    EditText etName,etMail,etPhone,etPass1,etPass2;
    String mail,pass1,pass2,name,phone;
    Button regButtom;
    Dialog outhErrorDialog;
    ProgressBar progressBar;
    List<String> errorList = new ArrayList<>();

    List<String> errorListmail = new ArrayList<>();
    List<String> errorListphone = new ArrayList<>();
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
        setContentView(R.layout.activity_registration_page);
        outhErrorDialog = new Dialog(this);
        setUi();
        Sprite foldingCube = new FoldingCube();
        foldingCube.setColor(Color.parseColor("#2073CC"));
        foldingCube.setScale(0.7f);
        progressBar.setIndeterminateDrawable(foldingCube);

        tvGologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationPage.this,LoginPage.class));
                finish();
            }
        });

        regButtom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                name = etName.getText().toString();
                mail = etMail.getText().toString();
                phone = etPhone.getText().toString();
                pass1 = etPass1.getText().toString();
                pass2 = etPass2.getText().toString();
                if(name.isEmpty())
                {
                    etName.setError("Provide Name");
                }
                if(phone.isEmpty())
                {
                    etPhone.setError("Phone No Required");
                }
                if(mail.isEmpty())
                {
                    etMail.setError("Mail Required");
                }
                if(pass1.isEmpty()||pass2.isEmpty())
                {
                    etPass1.setError("Password Required");
                    etPass2.setError("Conform Password Needed");
                }
                if(!pass1.equals(pass2))
                {
                    Toast.makeText(RegistrationPage.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                }else {

                    if(new NetworkOperator().checknetConnection(RegistrationPage.this))
                    {
                        if(name.isEmpty()||mail.isEmpty()||phone.isEmpty()||pass1.isEmpty()||pass2.isEmpty())
                        {
                            Toast.makeText(RegistrationPage.this, "Provide All Information", Toast.LENGTH_SHORT).show();
                        }else {
                            progressBar.setVisibility(View.VISIBLE);
                            regByAPI(name,mail,phone,pass1,pass2);
                        }

                    }else {

                        Toast.makeText(RegistrationPage.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private void regByAPI(String name, String mail, String phone, String pass1, String pass2) {

        Call<RegResponse> call = RetrifitClient.getInstance()
                .getRegAPi().registerUser(name,mail,phone,pass1,pass2);
        call.enqueue(new Callback<RegResponse>() {
            @Override
            public void onResponse(Call<RegResponse> call, Response<RegResponse> response) {
                try{
                    if (response.errorBody()!=null)
                    {
                        if(response.errorBody()==null){
                            progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            showAuthError(errorList);
                        }

                    }else {

                        if(response.body().getSuccess()!=null)
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            errorListmail = response.body().getError().getEmail();
                            errorListphone = response.body().getError().getPhone();

                            if(response.body().getError().getEmail()!=null)
                            {
                                showAuthError(errorListmail);
                            }

                            if (response.body().getError().getPhone()!=null)
                            {
                                showAuthError(errorListphone);
                            }

                        }else {

                        }

                        if(response.body().getStatus()==null){

                        }else {
                            if (response.body().getStatus())
                            {
                                //Toast.makeText(RegistrationPage.this, "Successful Account Creation"+response.body().getToken(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegistrationPage.this,OtpA.class);
                                savePreferences(response.body().getToken());
                                i.putExtra("token",response.body().getToken());
                                i.putExtra("number",phone);
                                i.putExtra("email",etMail.getText().toString());
                                i.putExtra("pass",etPass2.getText().toString());
                                startActivity(i);
                            }
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RegResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
               showAuthError(errorList);
            }
        });
    }

    private void setUi() {
        tvGologin = findViewById(R.id.tvGoLogin);
        regButtom = findViewById(R.id.btnRegister);
        etName = findViewById(R.id.etUserNmaeReg);
        etMail = findViewById(R.id.etMailReg);
        etPhone = findViewById(R.id.etPhoneReg);
        etPass1 = findViewById(R.id.etPassReg);
        etPass2 = findViewById(R.id.etconformPassReg);
        progressBar = findViewById(R.id.pbReg);

    }
    public void showAuthError(List<String> errorList) {
        progressBar.setVisibility(View.INVISIBLE);
        outhErrorDialog.setContentView(R.layout.reg_error_dialog);
        //retryButton = outhErrorDialog.findViewById(R.id.btn_retry_login);
        outhErrorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.colorAccent));
        outhErrorDialog.show();
        String errorText ="";
        Button retryButton = outhErrorDialog.findViewById(R.id.btn_retry_register);
        TextView tvError = outhErrorDialog.findViewById(R.id.tv_regError_desc);

        for (String erorstr :
                errorList) {
            errorText += "\n"+erorstr;
        }

        tvError.setText(errorText);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outhErrorDialog.dismiss();
            }
        });

    }
    private void savePreferences(String token) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        // Edit and commit
        UnameValue = mail;
        PasswordValue = pass2;
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.putString(TOKEN,token);
        editor.commit();
    }
}