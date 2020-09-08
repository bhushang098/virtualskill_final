package com.twilio.video.app.MainPages;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.FormPages.CreateNewSkill;
import com.twilio.video.app.FormPages.CreateTeamPage;
import com.twilio.video.app.HomePage;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SingleUserResponse.Data;
import com.twilio.video.app.SingleUserResponse.SingleUserResponse;
import com.twilio.video.app.adapter.SkillTabsAdapter;
import com.twilio.video.app.subMainPages.AvailableSkillFrag;
import com.twilio.video.app.subMainPages.HostedSkills;
import com.twilio.video.app.subMainPages.JoinedSkillFragment;
import com.twilio.video.app.subMainPages.SkillDetailsPage;
import com.twilio.video.app.ui.login.CommunityLoginActivity;
import com.twilio.video.app.ui.room.RoomActivity;
import com.twilio.video.app.ui.splash.SplashActivity;
import com.twilio.video.app.util.NetworkOperator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkillPage extends AppCompatActivity {

    OnSwipeTouchListenerSkills onSwipeTouchListenerSkills;
    Toolbar toolbar;
    Button newSkillButton;

    private SkillTabsAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    String token;
    Data userObj = new Data();

    NetworkOperator networkOperator = new NetworkOperator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_page);
        loadPreferences();
        if(networkOperator.checknetConnection(this))
        {
            getAndSaveUserData(token);
        }


        setUi();
        toolbar.setTitle("Skills");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);
        if (userObj.getCanCreateClass() == 0) {
            newSkillButton.setVisibility(View.GONE);
        }

        adapter = new SkillTabsAdapter(getSupportFragmentManager());
        adapter.addFragment(new AvailableSkillFrag(), "Available ");
        adapter.addFragment(new JoinedSkillFragment(), "Joined ");
        if(userObj.getCanCreateSkill()==1)
        {
            adapter.addFragment(new HostedSkills(), "Hosted ");
        }


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        onSwipeTouchListenerSkills = new OnSwipeTouchListenerSkills(this, findViewById(R.id.flSkills));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_skill);
        bottomNavigationView.setSelectedItemId(R.id.nav_skill);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(SkillPage.this, HomePage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_skill:
                        break;
                    case R.id.nav_teams:
                        startActivity(new Intent(SkillPage.this, TeamsPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_classes:
                        startActivity(new Intent(SkillPage.this, ClassesPage.class));
                        overridePendingTransition(0, 0);
                        finish();

                        break;

                    case R.id.nav_users:
                        startActivity(new Intent(SkillPage.this, UsersPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }

                return false;
            }
        });

        newSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SkillPage.this, CreateNewSkill.class);
                i.putExtra("token", token);
                i.putExtra("status","Make");
                Gson gson = new Gson();
                String userObjStr = gson.toJson(userObj);
                i.putExtra("skillObj","0");
                i.putExtra("userObj", userObjStr);

                startActivity(i);
            }
        });

    }

    private void setUi() {
        toolbar = findViewById(R.id.tbSkills);
        //joinBtn = findViewById(R.id.btn_join_room);
       // cvSkill = findViewById(R.id.cv_skill);
        newSkillButton = findViewById(R.id.btn_new_Skill);
        viewPager = (ViewPager) findViewById(R.id.vpg_skill);
        tabLayout = (TabLayout) findViewById(R.id.tbl_skill);
    }

    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = settings.getString("UserObj", "");
        userObj = gson.fromJson(json, Data.class);
        token = settings.getString("token", "");

    }

    private void getAndSaveUserData(String token) {

        Call<SingleUserResponse> call = RetrifitClient.getInstance()
                .getUserApi().getUSer(token);
        call.enqueue(new Callback<SingleUserResponse>() {
            @Override
            public void onResponse(Call<SingleUserResponse> call, Response<SingleUserResponse> response) {
                try{
                    if (response.body()==null)
                    {
                        if(response.errorBody()==null){
                            //progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            // showAuthError();
                        }

                    }else {
                        userObj = response.body().getData();
                        saveuserObj();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SingleUserResponse> call, Throwable t) {
                //progressBar.setVisibility(View.INVISIBLE);
                //showAuthError();
            }
        });
    }

    private void saveuserObj() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userObj);
        editor.putString("UserObj", json);
        editor.commit();
    }
}

class OnSwipeTouchListenerSkills implements View.OnTouchListener {
    private final GestureDetector gestureDetector;
    Context context;

    OnSwipeTouchListenerSkills(Context ctx, View mainView) {

        gestureDetector = new GestureDetector(ctx, new OnSwipeTouchListenerSkills.GestureListener());
        mainView.setOnTouchListener(this);
        context = ctx;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public class GestureListener extends
            GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    void onSwipeRight() {
//        Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
        context.startActivity(new Intent(context,HomePage.class));
        Activity activity = (Activity) context;
        activity.finish();
        activity.overridePendingTransition(0, 0);
        this.onSwipe.swipeRight();
    }

    void onSwipeLeft() {
        context.startActivity(new Intent(context,TeamsPage.class));
        Activity activity = (Activity) context;
        activity.finish();
        activity.overridePendingTransition(0, 0);
        this.onSwipe.swipeLeft();
    }

    void onSwipeTop() {
//        Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();
        this.onSwipe.swipeTop();
    }

    void onSwipeBottom() {
//        Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();
        this.onSwipe.swipeBottom();
    }

    interface onSwipeListener {
        void swipeRight();

        void swipeTop();

        void swipeBottom();

        void swipeLeft();
    }

    onSwipeListener onSwipe;
}