package com.twilio.video.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.ChatUserResponse.ChatUserResponse;
import com.twilio.video.app.ChatUserResponse.Message;
import com.twilio.video.app.adapter.ChatUSerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatScreen extends AppCompatActivity {
    TextView tvUserOrMessages, tvChatStatus;
    ImageView ivToggleMessview, ivSendmessage;
    RecyclerView recViewAvailableUSer, recViewChatMessage;
    EditText etMessage;
    List<Message> userList = new ArrayList<>();
    String token;
    ProgressBar progressBar;
    String userId;

    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token", "");
        Gson gson = new Gson();
        UserObj userobj;
        String userobjStr = settings.getString("UserObj", "");
        userobj = gson.fromJson(userobjStr, UserObj.class);
        userId = userobj.getId().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        loadPreferences();
        setUi();
        recViewAvailableUSer.setVisibility(View.VISIBLE);
        loadchatUsers();

    }

    private void loadchatUsers() {

        progressBar.setVisibility(View.VISIBLE);

        Call<ChatUserResponse> call = RetrifitClient.getInstance().getChatApi()
                .getChatUsers(token);
        call.enqueue(new Callback<ChatUserResponse>() {
            @Override
            public void onResponse(Call<ChatUserResponse> call, Response<ChatUserResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(">>>Chat Response", response.raw().toString());

                if(response.body() == null) {

                } else {
                    userList = response.body().getMessages();
                    if (userList.size() > 0) {
                        tvChatStatus.setVisibility(View.GONE);
                        //toDo render Users
                        recViewAvailableUSer.setLayoutManager(new LinearLayoutManager(ChatScreen.this));
                        recViewAvailableUSer.setAdapter(new ChatUSerAdapter(userList, ChatScreen.this, token, userId));
                    } else {
                        tvChatStatus.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<ChatUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(">>Exceptopn>>", t.toString());
            }
        });


    }

    private void setUi() {
        tvUserOrMessages = findViewById(R.id.tv_user_or_chat);
        recViewAvailableUSer = findViewById(R.id.rec_view_chat_user_available);
        recViewChatMessage = findViewById(R.id.rec_view_detailed_chat_with_user);
        ivToggleMessview = findViewById(R.id.iv_toggle_chat);
        ivSendmessage = findViewById(R.id.iv_send_chat_message);
        etMessage = findViewById(R.id.et_message);
        tvChatStatus = findViewById(R.id.tv_chat_status);
        progressBar = findViewById(R.id.pb_chat_user_list);
    }
}