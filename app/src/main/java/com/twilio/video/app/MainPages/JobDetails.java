package com.twilio.video.app.MainPages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.gson.Gson;
import com.twilio.video.app.JobsResponse.Datum;
import com.twilio.video.app.R;

public class JobDetails extends AppCompatActivity {

    TextView title,location,salary,desc,lastDate;
    Button applyBtn;
    Datum jobObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        setUi();
        Gson json = new Gson();

        jobObj = json.fromJson(getIntent().getStringExtra("jobObj"),Datum.class);

        setJobData();

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODo Apply By API
            }
        });

    }

    private void setJobData() {
        title.setText(jobObj.getTitle());
        location.setText(jobObj.getLocation());
        salary.setText(jobObj.getSalary());
        desc.setText(jobObj.getDescription());
        lastDate.setText(jobObj.getLastDate());
    }

    private void setUi() {
        title = findViewById(R.id.tv_job_name_on_tb);
        location = findViewById(R.id.tv_job_location);
        salary = findViewById(R.id.tv_job_salary);
        desc = findViewById(R.id.tv_job_overview);
        lastDate = findViewById(R.id.tv_last_date_to_apply);
        applyBtn = findViewById(R.id.btn_job_apply);

    }
}