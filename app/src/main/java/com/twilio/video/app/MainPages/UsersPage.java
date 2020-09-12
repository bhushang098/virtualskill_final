package com.twilio.video.app.MainPages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.twilio.video.app.HomePage;
import com.twilio.video.app.R;
import com.twilio.video.app.adapter.UsersTabAdapter;
import com.twilio.video.app.subMainPages.FollowerUsersFrag;
import com.twilio.video.app.subMainPages.FollowingUsersFrag;
import com.twilio.video.app.subMainPages.HrListFrag;
import com.twilio.video.app.subMainPages.ProListFrag;
import com.twilio.video.app.subMainPages.StudentsListFrag;

public class UsersPage extends AppCompatActivity {

    Toolbar toolbar;
    private UsersTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_page);
        setUi();
        String Title = getIntent().getStringExtra("uType");
        toolbar.setTitle("Users");
        if(Title!=null)
        {
            if(Title.equalsIgnoreCase("Pro"))
            {
                toolbar.setTitle("Professor");
            }else {
                toolbar.setTitle("Students");
            }
        }


        setSupportActionBar(toolbar);

        adapter = new UsersTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProListFrag(), "Experts ");
        adapter.addFragment(new StudentsListFrag(), "Professionals ");
        adapter.addFragment(new HrListFrag(),"HR's");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_users);
        bottomNavigationView.setSelectedItemId(R.id.nav_users);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(UsersPage.this, HomePage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_skill:
                        startActivity(new Intent(UsersPage.this, SkillPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_teams:
                        startActivity(new Intent(UsersPage.this, TeamsPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_classes:
                        startActivity(new Intent(UsersPage.this, ClassesPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        break;

                    case R.id.nav_users:
                        break;
                }

                return false;
            }
        });
    }

    private void setUi() {
        toolbar = findViewById(R.id.tbUsers);
        viewPager = (ViewPager) findViewById(R.id.vpg_users);
        tabLayout = (TabLayout) findViewById(R.id.tbl_users);
    }
}

