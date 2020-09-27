package com.twilio.video.app.MainPages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.gson.Gson;
import com.twilio.video.app.Dialogs.ConformationDialog;
import com.twilio.video.app.HomePage;
import com.twilio.video.app.JobsResponse.Datum;
import com.twilio.video.app.R;

public class JobDetails extends AppCompatActivity {

    TextView title,location,salary,desc,lastDate;
    Button applyBtn;
    Datum jobObj;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        setUi();
        loadPrefs();
        Gson json = new Gson();

        jobObj = json.fromJson(getIntent().getStringExtra("jobObj"),Datum.class);
        if(getIntent().getStringExtra("status").equalsIgnoreCase("applied"))
        {
            setJobData("Applied");
        }else {
            setJobData("Apply");
        }



        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(applyBtn.getText().equals("Apply"))
                showConformationBox();
                else
                    Toast.makeText(JobDetails.this, "Already Applied", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadPrefs() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);

        token = settings.getString("token", "");
    }

    private void showConformationBox() {
        ConformationDialog conformationDialog = new ConformationDialog(JobDetails.this);
        conformationDialog.showConformDialog(JobDetails.this,"Application",
                "Do You Want To Apply For Job "+jobObj.getTitle(),"apply_job"+",>>>"
                        +jobObj.getId()+",>>>"+token);
    }

    private void setJobData(String status) {
        title.setText(jobObj.getTitle());
        location.setText(jobObj.getLocation());
        salary.setText(jobObj.getSalary());
        desc.setText(jobObj.getDescription());
        lastDate.setText(jobObj.getLastDate());
        applyBtn.setText(status);
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