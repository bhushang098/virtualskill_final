package com.twilio.video.app.subMainPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.twilio.video.app.FollowingListArgs.FollowingList;
import com.twilio.video.app.FollowingListArgs.FollowingListArgs;
import com.twilio.video.app.FollowingUserResponse.Datum;
import com.twilio.video.app.FollowingUserResponse.FollowingUserREsponse;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.adapter.FollworingUserAdapter;
import com.twilio.video.app.adapter.FollworingUserAdapter2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingPage extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView revFolloeings;
    private List<Datum> followingsList = new ArrayList<>();
    private List<FollowingList> followingLists2 = new ArrayList<>();
    TextView tvfolllowingsEmpty;
    FollowingListArgs args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_page);
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        Gson json = new Gson();
        String s = getIntent().getStringExtra("following_list");

        setUi();
        if(s==null)
        {
            loadFollowings(settings.getString("token",""));
        }
        else{
            args = json.fromJson(s,FollowingListArgs.class);
            followingLists2 = args.getFollowingList();
            setData2(settings.getString("token",""));
        }

    }

    private void setData2(String token) {
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
        revFolloeings.setLayoutManager(new LinearLayoutManager(FollowingPage.this));
        revFolloeings.setAdapter(new FollworingUserAdapter2(followingLists2,FollowingPage.this,token));
    }

    private void setUi() {
        revFolloeings = findViewById(R.id.recViewFollowingUsersPage);
        shimmerFrameLayout = findViewById(R.id.sh_v_following_page);
        shimmerFrameLayout.startShimmerAnimation();
        tvfolllowingsEmpty = findViewById(R.id.tv_following_user_item_not_available_page);

    }

    private void loadFollowings(String token) {
        Call<FollowingUserREsponse> call =  RetrifitClient
                .getInstance().getUserApi().getFollowingUSers(token);

        call.enqueue(new Callback<FollowingUserREsponse>() {
            @Override
            public void onResponse(Call<FollowingUserREsponse> call, Response<FollowingUserREsponse> response) {
                Log.d("Resposne USers>>", response.raw().toString());
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                if(response.body()!=null)
                {
                    if (response.body().getStatus())
                    {
                        followingsList = response.body().getData();
                        if(followingsList.size()>0)
                        {
                            revFolloeings.setLayoutManager(new LinearLayoutManager(FollowingPage.this));
                            revFolloeings.setAdapter(new FollworingUserAdapter(followingsList,FollowingPage.this,token));

                        }else {
                            tvfolllowingsEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowingUserREsponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }
}