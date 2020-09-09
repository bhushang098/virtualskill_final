package com.twilio.video.app.FormPages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.CreatedClassResponse.CreatedClassRespones;
import com.twilio.video.app.HomePage;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SkillSingleResponse.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewSkill extends AppCompatActivity {

    String[] whoCanPost = { "Members", "EveryOne"};
    String[] WhoCanSee = { "Members", "EveryOne"};
    Spinner spinpost,spinsee;
    Button createSkillButtion,deleteSkillBtn;
    EditText etSkillNAme,etFee,etAbout;
    Toolbar toolbar;

    Data skillObj;

    String skillName,whoCanPoststr,whoCanSeePost,fees,about,token,status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_skill);
        token = getIntent().getStringExtra("token");
        status = getIntent().getStringExtra("status");
        setUi();
        if(getIntent().getStringExtra("skillObj").equalsIgnoreCase("0"))
        {

        }else {
            Gson gson = new Gson();
            skillObj = gson.fromJson(getIntent().getStringExtra("skillObj"),Data.class);

        }
        if(status.equalsIgnoreCase("Edit"))
        {
            deleteSkillBtn.setVisibility(View.VISIBLE);
            createSkillButtion.setText(" Update ");
            toolbar.setTitle("Edit "+skillObj.getName());
            setOldSkillDeta();
        }

        setAdpater();

        spinpost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                whoCanPoststr = whoCanPost[position];
                //Toast.makeText(CreateNewSkill.this, whoCanPost[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinsee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                whoCanSeePost = WhoCanSee[position];
               // Toast.makeText(CreateNewSkill.this, WhoCanSee[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        createSkillButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ToDo MAke New Skillskill

                skillName = etSkillNAme.getText().toString();
                fees = etFee.getText().toString();
                about = etAbout.getText().toString();
                if(skillName.isEmpty()||fees.isEmpty()||about.isEmpty())
                {
                    Toast.makeText(CreateNewSkill.this, "Please Provide All Information", Toast.LENGTH_SHORT).show();
                }else {
                    if(status.equalsIgnoreCase("Make"))
                    makenewSkill();
                    else
                        updateSkiil(skillObj.getId().toString());
                }

            }
        });
        deleteSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSkill();
            }
        });
    }

    private void deleteSkill() {
        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getSkillApi().deleteSkill("skill/delete/"+skillObj.getId().toString(),token);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response){
                Log.d("TREsponse>>",response.raw().toString());

                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        Toast.makeText(CreateNewSkill.this, "Skill Deleted", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(CreateNewSkill.this, HomePage.class));
                        finish();
                        finish();
                    }else {
                        Toast.makeText(CreateNewSkill.this, "Error Deleting ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {

            Log.d("Error", t.toString());

            }
        });

    }

    private void setOldSkillDeta() {

        etSkillNAme.setText(skillObj.getName());
        etFee.setText(skillObj.getFee());
        etAbout.setText(skillObj.getAbout());
        if(skillObj.getWhoCanPost().equalsIgnoreCase("EveryOne"))
        {
            spinpost.setSelection(1);
            whoCanPoststr = whoCanPost[1];
        }
        else
        {
            spinpost.setSelection(0);
            whoCanPoststr = whoCanPost[0];
        }


        if(skillObj.getWhoCanSee().equalsIgnoreCase("EveryOne"))
        {
            spinsee.setSelection(1);
            whoCanSeePost = WhoCanSee[1];
        }
        else
        {
            spinsee.setSelection(0);
            whoCanSeePost = WhoCanSee[0];
        }


    }

    private void updateSkiil(String skillId) {

        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getSkillApi().updateSkill("skill/edit/"+skillId,token,
                        skillName,whoCanPoststr,whoCanSeePost,fees,about);
        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d(">response>>",response.raw().toString() );
                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        Toast.makeText(CreateNewSkill.this, "Skill Updated Successfully", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(CreateNewSkill.this, HomePage.class));
                        finish();
                        finish();
                    }else {
                        Toast.makeText(CreateNewSkill.this, "Error Updating ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {

            }
        });


    }

    private void makenewSkill() {

        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getSkillApi().makeNewSkill(token,skillName,whoCanPoststr,whoCanSeePost,fees,about);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("Response Skill>>",response.raw().toString());
                        if(response.body()!=null)
                        {
                            Toast.makeText(CreateNewSkill.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Log.d(">>Exception>>", t.toString());
            }
        });

    }

    private void setAdpater() {
        ArrayAdapter<String> adapterpost = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, whoCanPost);
        adapterpost.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinpost.setAdapter(adapterpost);

        ArrayAdapter<String> adapterSee = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,WhoCanSee);
        adapterSee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinsee.setAdapter(adapterSee);
    }

    private void setUi() {
        spinpost = findViewById(R.id.spinner_who_can_post_to_skill);
        spinsee = findViewById(R.id.spinner_who_can_see_post_on_skill);
        createSkillButtion = findViewById(R.id.btn_commit_new_skill);
        deleteSkillBtn = findViewById(R.id.btn_delete_skill);
        etSkillNAme = findViewById(R.id.et_new_skill_name);
        etFee = findViewById(R.id.et_new_skill_fees);
        etAbout = findViewById(R.id.et_about_new_skill);
        toolbar = findViewById(R.id.tb_new_skill);
    }

}