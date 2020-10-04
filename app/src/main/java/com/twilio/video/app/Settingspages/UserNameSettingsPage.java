package com.twilio.video.app.Settingspages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.twilio.video.app.MainPages.SettingsActivity;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SettingsResponse.SettingsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNameSettingsPage extends AppCompatActivity {
    String userNAme;
    EditText etUsername;
    TextView tvUpdate;
    String token;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_settings_page);
        userNAme = getIntent().getStringExtra("user_name");
        loadToken();
        setUi();
        etUsername.setText(userNAme);

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNAme = etUsername.getText().toString();
                updateUserName();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadToken() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token", "");

    }

    private void updateUserName() {
        Call<SettingsResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().updateUserName(token, userNAme);

        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                Log.d("Response>>>", response.raw().toString());

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(UserNameSettingsPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UserNameSettingsPage.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

            }
        });
    }

    private void setUi() {
        etUsername = findViewById(R.id.et_userName_on_settings);
        tvUpdate = findViewById(R.id.tv_update_user_name);
        ivBack = findViewById(R.id.iv_back_change_u_name);
    }
}