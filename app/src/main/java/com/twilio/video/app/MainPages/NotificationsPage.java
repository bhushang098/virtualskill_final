package com.twilio.video.app.MainPages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.NotificationAlertResponse.Datum;
import com.twilio.video.app.NotificationAlertResponse.NotificationAlerrtResponse;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.adapter.NotificationAlertAdapter;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsPage extends AppCompatActivity {

    RecyclerView recyclerView;
    String token;
    List<Datum> notiList;
    String hisId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_page);
        token = getIntent().getStringExtra("token");
        loadprefs();
        setUi();
        loadNotifications();

    }

    private void loadprefs() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
       Gson json = new Gson();
       hisId = json.fromJson(settings.getString("UserObj",""), UserObj.class).getId().toString();


    }

    private void loadNotifications() {
        Call<NotificationAlerrtResponse> call = RetrifitClient.getInstance()
                .getNotificationApi().getNotificationList(token);

        call.enqueue(new Callback<NotificationAlerrtResponse>() {
            @Override
            public void onResponse(Call<NotificationAlerrtResponse> call, Response<NotificationAlerrtResponse> response) {
                Log.d("Response>>", response.raw().toString());

                if(response.body()!=null)
                {
                    notiList = response.body().getData().getData();
                    Collections.reverse(notiList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsPage.this));
                    recyclerView.setAdapter(new NotificationAlertAdapter(notiList, NotificationsPage.this, hisId, token));
                }
            }

            @Override
            public void onFailure(Call<NotificationAlerrtResponse> call, Throwable t) {

                Log.d("Exception>>", t.toString());
            }
        });
    }

    private void setUi() {
        recyclerView = findViewById(R.id.rec_v_notifications);
    }
}