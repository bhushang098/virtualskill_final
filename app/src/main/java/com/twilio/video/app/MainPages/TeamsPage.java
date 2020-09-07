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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.FormPages.CreateClassPage;
import com.twilio.video.app.FormPages.CreateTeamPage;
import com.twilio.video.app.HomePage;
import com.twilio.video.app.R;
import com.twilio.video.app.adapter.SkillTabsAdapter;
import com.twilio.video.app.adapter.TemasTabAdapter;
import com.twilio.video.app.subMainPages.AvailableSkillFrag;
import com.twilio.video.app.subMainPages.AvailableTeamsFrag;
import com.twilio.video.app.subMainPages.HostedSkills;
import com.twilio.video.app.subMainPages.HostedTeamsFrag;
import com.twilio.video.app.subMainPages.JoinedSkillFragment;
import com.twilio.video.app.subMainPages.JoinedTeamsFrag;

public class TeamsPage extends AppCompatActivity {

    OnSwipeTouchListenerTeams onSwipeTouchListenerTeams;
    Toolbar toolbar;

    private TemasTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Button newTeamButton;
    String token;
    UserObj userObj = new UserObj();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas_page);
        loadPreferences();
        setUi();
        toolbar.setTitle("Teams");
        setSupportActionBar(toolbar);

        adapter = new TemasTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new AvailableTeamsFrag(), "Available ");
        adapter.addFragment(new JoinedTeamsFrag(), "Joined ");
        adapter.addFragment(new HostedTeamsFrag(), "Hosted ");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        onSwipeTouchListenerTeams = new OnSwipeTouchListenerTeams(this, findViewById(R.id.flTeams));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_team);
        bottomNavigationView.setSelectedItemId(R.id.nav_teams);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(TeamsPage.this, HomePage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_skill:
                        startActivity(new Intent(TeamsPage.this, SkillPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_teams:
                        break;
                    case R.id.nav_classes:
                        startActivity(new Intent(TeamsPage.this, ClassesPage.class));
                        overridePendingTransition(0, 0);
                        finish();

                        break;

                    case R.id.nav_users:
                        startActivity(new Intent(TeamsPage.this, UsersPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }

                return false;
            }
        });

        newTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeamsPage.this, CreateTeamPage.class);
                i.putExtra("token", token);
                i.putExtra("status","Make");
                Gson gson = new Gson();
                String userObjStr = gson.toJson(userObj);
                i.putExtra("teamObj","0");
                i.putExtra("userObj", userObjStr);

                startActivity(i);
            }
        });
    }

    private void setUi() {
        toolbar = findViewById(R.id.tbTeams);
        viewPager = (ViewPager) findViewById(R.id.vpg_teams);
        tabLayout = (TabLayout) findViewById(R.id.tbl_teams);
        newTeamButton = findViewById(R.id.btn_new_team);

    }
    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = settings.getString("UserObj", "");
        userObj = gson.fromJson(json, UserObj.class);
        token = settings.getString("token", "");

    }
}

class OnSwipeTouchListenerTeams implements View.OnTouchListener {
    private final GestureDetector gestureDetector;
    Context context;

    OnSwipeTouchListenerTeams(Context ctx, View mainView) {

        gestureDetector = new GestureDetector(ctx, new OnSwipeTouchListenerTeams.GestureListener());
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
        context.startActivity(new Intent(context,SkillPage.class));
        Activity activity = (Activity) context;
        activity.finish();
        activity.overridePendingTransition(0, 0);
        this.onSwipe.swipeRight();
    }

    void onSwipeLeft() {
        context.startActivity(new Intent(context,ClassesPage.class));
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