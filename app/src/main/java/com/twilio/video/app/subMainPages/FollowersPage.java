package com.twilio.video.app.subMainPages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.twilio.video.app.FollowerUserResponse.Datum;
import com.twilio.video.app.FollowerUserResponse.FollowerUserResponse;
import com.twilio.video.app.FollowersListArgs.FolllowerListArgs;
import com.twilio.video.app.FollowersListArgs.FollowersList;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.adapter.FollowerUserAdapter;
import com.twilio.video.app.adapter.FollowersUserAdapter2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersPage extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView revFollowers;
    private List<Datum> followerList = new ArrayList<>();
    List<FollowersList> followerList2 = new ArrayList<>();
    TextView tvFollowerEmpty;
    FolllowerListArgs args;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_page);
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        Gson json = new Gson();
        String s = getIntent().getStringExtra("followers_list");

        setUi();
        if(s==null)
        {
            loadFollowers(settings.getString("token",""));
        }
        else{
            args = json.fromJson(s, FolllowerListArgs.class);
            followerList2 = args.getFollowersList();
            setData2(settings.getString("token",""));
        }

    }

    private void setData2(String token) {
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
        revFollowers.setLayoutManager(new LinearLayoutManager(FollowersPage.this));
        revFollowers.setAdapter(new FollowersUserAdapter2(followerList2,FollowersPage.this,token));
    }

    private void setUi() {
        revFollowers = findViewById(R.id.recViewFollowerUsersPage);
        shimmerFrameLayout = findViewById(R.id.sh_v_followers_page);
        shimmerFrameLayout.startShimmerAnimation();
        tvFollowerEmpty = findViewById(R.id.tv_follower_user_item_not_available_page);
    }

    private void loadFollowers(String token) {
        Call<FollowerUserResponse> call = RetrifitClient.getInstance()
                .getUserApi().getFollowers(token);

        call.enqueue(new Callback<FollowerUserResponse>() {
            @Override
            public void onResponse(Call<FollowerUserResponse> call, Response<FollowerUserResponse> response) {
                Log.d("Suer Response>>", response.raw().toString());
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                if(response.body()!=null)
                {

                    if (response.body().getStatus())
                    {
                        followerList = response.body().getData();
                        if(followerList.size()>0)
                        {
                            revFollowers.setLayoutManager(new LinearLayoutManager(FollowersPage.this));
                            revFollowers.setAdapter(new FollowerUserAdapter(followerList,FollowersPage.this,token));
                        }else {
                            tvFollowerEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowerUserResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }
}