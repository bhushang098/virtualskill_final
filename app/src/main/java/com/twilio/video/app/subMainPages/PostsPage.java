package com.twilio.video.app.subMainPages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.HomePostModal.Datum;
import com.twilio.video.app.HomePostModal.HomePostModal;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.adapter.HomePostsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsPage extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;
    String token;
    private List<Datum> postDataList = new ArrayList<>();
    int hisUserId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_page);
        loadPreferences();
        setUi();
        toolbar.setTitle(getIntent().getStringExtra("user_name"));
        loadUserPosts(getIntent().getStringExtra("otherUserId"));
    }

    private void loadUserPosts(String otherUserId) {

        Call<HomePostModal> call = RetrifitClient.getInstance()
                .getPostApi().getPostByFilter(token,"self",otherUserId);
        call.enqueue(new Callback<HomePostModal>() {
            @Override
            public void onResponse(Call<HomePostModal> call, Response<HomePostModal> response) {
                try{
                    if(response.body() == null){
                        Log.d("Error>>", response.errorBody().string());

                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);

                    }else {
                            postDataList = response.body().getPosts().getData();
                            recyclerView.setLayoutManager(new LinearLayoutManager(PostsPage.this));
                            recyclerView.setAdapter(new HomePostsAdapter(postDataList,PostsPage.this,hisUserId,token));
                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<HomePostModal> call, Throwable t) {
             //   Toast.makeText(HomePage.this, "Error ", Toast.LENGTH_SHORT).show();

                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }


    private void setUi() {
        shimmerFrameLayout = findViewById(R.id.sh_v_post_page);
        recyclerView = findViewById(R.id.recViewPostPage);
        toolbar = findViewById(R.id.tb_posts_page);

    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);

        token = settings.getString("token","");
        Gson json = new Gson();
        hisUserId = json.fromJson(settings.
                getString("UserObj",""),UserObj.class).getId();



    }


}