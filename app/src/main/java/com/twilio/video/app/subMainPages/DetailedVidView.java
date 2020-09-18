package com.twilio.video.app.subMainPages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.potyvideo.library.AndExoPlayerView;
import com.twilio.video.app.R;

public class DetailedVidView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_vid_view);
        AndExoPlayerView andExoPlayerView =

                findViewById(R.id.detailed_video_view);
        andExoPlayerView.setSource(getIntent().getStringExtra("vid_file"));
        andExoPlayerView.setBackgroundColor(Color.CYAN);

    }
}