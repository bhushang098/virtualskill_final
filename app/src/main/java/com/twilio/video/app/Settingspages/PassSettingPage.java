package com.twilio.video.app.Settingspages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twilio.video.app.MainPages.SettingsActivity;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SettingsResponse.SettingsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassSettingPage extends AppCompatActivity {
    EditText etCurrentPass, etNewPass1, etnewPass2;
    TextView tvUpdatePass;
    String currentPass, newPass1, newPass2,token;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_setting_page);
        setUi();
        loadToken();

        tvUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPass = etCurrentPass.getText().toString();
                newPass1 = etNewPass1.getText().toString();
                newPass2 = etnewPass2.getText().toString();
                if (currentPass.isEmpty() || newPass1.isEmpty() || newPass2.isEmpty()) {
                    Toast.makeText(PassSettingPage.this, "PassWord Missing", Toast.LENGTH_SHORT).show();
                } else {
                    if (newPass1.equals(newPass2)) {
                        // ToDo UpDate New Pass
                        updatePassword();

                    } else {
                        Toast.makeText(PassSettingPage.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUi() {
        etCurrentPass = findViewById(R.id.et_current_pass_on_settings);
        etNewPass1 = findViewById(R.id.et_new_pass1_on_settings);
        etnewPass2 = findViewById(R.id.et_new_pass2_on_settings);
        ivBack = findViewById(R.id.iv_back_pass_setting);
        tvUpdatePass = findViewById(R.id.tv_update_pass);

    }

    private void loadToken() {

        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token", "");

    }

    private void updatePassword() {
        Call<SettingsResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().updatePass(token, currentPass, newPass1, newPass2);

        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                Log.d("Response>>", response.raw().toString());

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(PassSettingPage.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(PassSettingPage.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {
                Log.d(">>Error>>", t.toString());
            }
        });

    }
}