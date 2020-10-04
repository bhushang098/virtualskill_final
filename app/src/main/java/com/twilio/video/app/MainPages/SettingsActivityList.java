package com.twilio.video.app.MainPages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.Settingspages.AccountSettingsPage;
import com.twilio.video.app.Settingspages.PassSettingPage;
import com.twilio.video.app.Settingspages.RoleSetingsPage;
import com.twilio.video.app.Settingspages.UserNameSettingsPage;
import com.twilio.video.app.SingleUserResponse.Data;
import com.twilio.video.app.SingleUserResponse.SingleUserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivityList extends AppCompatActivity {
    LinearLayout goAccnt,gouName,goRole,goPasss;
    ImageView ibBack;
    Data userObj = new Data();
    String token;
    EditText etUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);
        setUi();
        loadrefs();
        getUserByApi();
        gouName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userObj.getUsername()==null)
                {
                    Toast.makeText(SettingsActivityList.this, "Something Went Wrong please Retry", Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(SettingsActivityList.this,UserNameSettingsPage.class);
                    i.putExtra("user_name",userObj.getUsername());
                    startActivity(i);
                }

            }
        });
        goAccnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingsActivityList.this, AccountSettingsPage.class));
            }
        });
        goRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userObj.getUserType()==null)
                {
                    Toast.makeText(SettingsActivityList.this, "Something Went Wrong please Retry", Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(SettingsActivityList.this,RoleSetingsPage.class);
                    i.putExtra("status",userObj.getUserType().toString());
                    startActivity(i);
                }

            }
        });
        goPasss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivityList.this, PassSettingPage.class));
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadrefs() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token", "");
    }

    private void setUi() {
        gouName = findViewById(R.id.lin_lay_go_u_name_settings);
        goRole = findViewById(R.id.lin_lay_go_role_settings);
        goPasss = findViewById(R.id.lin_lay_go_pass_settings);
        goAccnt = findViewById(R.id.lin_lay_go_acct_settings);
        ibBack = findViewById(R.id.iv_back_settingsList);

        etUsername = findViewById(R.id.et_userName_on_settings);
    }

    private void getUserByApi() {

        Call<SingleUserResponse> call = RetrifitClient.getInstance()
                .getUserApi().getUSer(token);
        call.enqueue(new Callback<SingleUserResponse>() {
            @Override
            public void onResponse(Call<SingleUserResponse> call, Response<SingleUserResponse> response) {
                Log.d("User>obJResponse>",response.raw().toString());
                try{
                    if (response.body()==null)
                    {
                        //progressPopup.dismiss();
                        if(response.errorBody()==null){
                            //progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            // showAuthError();
                        }
                    }else {
                        //progressPopup.dismiss();
                        userObj = response.body().getData();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Exception>>",e.toString());
                    // progressPopup.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SingleUserResponse> call, Throwable t) {
                Toast.makeText(SettingsActivityList.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}