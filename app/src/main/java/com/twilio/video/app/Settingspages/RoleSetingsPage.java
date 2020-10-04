package com.twilio.video.app.Settingspages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.twilio.video.app.MainPages.SettingsActivity;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SettingsResponse.SettingsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoleSetingsPage extends AppCompatActivity {
    TextView tvproStatus;
    Button apply;
    ImageView ibBack;
    String proStat,token;
    PopupWindow progressPopu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_setings_page);
        proStat = getIntent().getStringExtra("status");
        setui();
        loadToken();

        if(proStat.equalsIgnoreCase("2"))
        {
         tvproStatus.setVisibility(View.VISIBLE);
         tvproStatus.setText("Your Application For Professor is Under Review");
         apply.setVisibility(View.GONE);
        }
        if(proStat.equalsIgnoreCase("1"))
        {
            tvproStatus.setVisibility(View.VISIBLE);
            tvproStatus.setText("Your Application For Professor is Approved");
            apply.setVisibility(View.GONE);
        }
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgressPopup(RoleSetingsPage.this);
                updateRole();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void startProgressPopup(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.progres_popup,
                null); // inflating popup layout
        progressPopu = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        progressPopu.setAnimationStyle(android.R.style.Animation_Dialog);
        progressPopu.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
    }


    private void loadToken() {

        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token", "");

    }

    private void setui() {
        tvproStatus = findViewById(R.id.tv_pro_status);
        ibBack = findViewById(R.id.iv_back_edit_role);
        apply = findViewById(R.id.btn_apply_for_pro);

    }

    private void updateRole() {
        Call<SettingsResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().applyPro(token);

        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                Log.d("Tag>response >>", response.raw().toString());

                progressPopu.dismiss();
                if(response.body()!=null)
                {
                    if (response.body().getStatus())
                    {
                        tvproStatus.setVisibility(View.VISIBLE);
                        tvproStatus.setText("Application For Professor is being Submitted");
                        apply.setVisibility(View.GONE);
                        Toast.makeText(RoleSetingsPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(RoleSetingsPage.this, "Error Updating ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {
                progressPopu.dismiss();
            }
        });
    }

}