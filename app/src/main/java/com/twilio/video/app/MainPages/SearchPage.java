package com.twilio.video.app.MainPages;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.twilio.video.app.R;
import com.twilio.video.app.SearchPages.SearchClassFrag;
import com.twilio.video.app.SearchPages.SearchPostFrag;
import com.twilio.video.app.SearchPages.SearchTeamsFrag;
import com.twilio.video.app.SearchPages.SearchUserFrag;
import com.twilio.video.app.adapter.SearchTabAdapter;

public class SearchPage extends AppCompatActivity {



    ImageView ivsearch;
    EditText etseatchkey;

    String token;
    Toolbar toolbar;


    private SearchTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        token = getIntent().getStringExtra("token");
        setUi();
        setSupportActionBar(toolbar);
        ivsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    setTAblayout(etseatchkey.getText().toString().trim());

            }
        });

        etseatchkey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeybaord(v);
                    setTAblayout(etseatchkey.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    private void setTAblayout(String key) {

        adapter = new SearchTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchUserFrag(), "Users",key);
        adapter.addFragment(new SearchClassFrag(), "Classes",key);
        adapter.addFragment(new SearchTeamsFrag(), "Teams",key);
        adapter.addFragment(new SearchPostFrag(), "Posts",key);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setUi() {
        toolbar = findViewById(R.id.tb_search);
        viewPager =  findViewById(R.id.vpg_search);
        tabLayout =  findViewById(R.id.tbl_search);
        ivsearch = findViewById(R.id.iv_make_serach);
        etseatchkey = findViewById(R.id.et_search);
    }
}