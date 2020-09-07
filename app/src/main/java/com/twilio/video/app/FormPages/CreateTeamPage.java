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
import com.twilio.video.app.TeamResponse.Datum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTeamPage extends AppCompatActivity {

    String[] whoCanPost = { "Members", "EveryOne"};
    String[] WhoCanSee = { "Members", "EveryOne"};
    String[] whoCanMess = { "Members", "EveryOne"};
    Spinner spinpost,spinsee,spinmess;
    Button createTeamButtion,deleteButton;
    EditText etTeamAnem,etLocation,etAbout;
    Toolbar toolbar;
    Datum teamObj = new Datum();
    String teamNAme,location,whoCanPoststr,whoCanseeStr,whoCanMessstr,about,token,status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team_page);
        token = getIntent().getStringExtra("token");
        status = getIntent().getStringExtra("status");
        setUi();
        if(getIntent().getStringExtra("teamObj").equalsIgnoreCase("0"))
        {

        }else {
            Gson gson = new Gson();
            teamObj = gson.fromJson(getIntent().getStringExtra("teamObj"), Datum.class);
        }
        if(status.equalsIgnoreCase("Edit"))
        {
            deleteButton.setVisibility(View.VISIBLE);
            createTeamButtion.setText(" Update ");
            toolbar.setTitle("Edit "+teamObj.getName());
            setOldTeamsDeta();
        }
        setAdaptersToSpinnsers();

        spinpost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                whoCanPoststr = whoCanPost[position];
                //Toast.makeText(CreateTeamPage.this, whoCanPost[position],    Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinsee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                whoCanseeStr = WhoCanSee[position];
               // Toast.makeText(CreateTeamPage.this, WhoCanSee[position],    Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinmess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                whoCanMessstr = whoCanMess[position];
                //Toast.makeText(CreateTeamPage.this, whoCanMess[position],    Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createTeamButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamNAme = etTeamAnem.getText().toString();
                location = etLocation.getText().toString();
                about = etAbout.getText().toString();

                if(teamNAme.isEmpty()||location.isEmpty()||about.isEmpty())
                {
                    Toast.makeText(CreateTeamPage.this, "Please provide All Information", Toast.LENGTH_SHORT).show();
                }else {
                    if(status.equalsIgnoreCase("Make"))
                        makeNewTeam(token);
                    else
                        updateTeam(teamObj.getId().toString());
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTeam();
            }
        });

    }

    private void deleteTeam() {
        Call<MakeClassResponse> cal = RetrifitClient.getInstance().getTeamsApi()
                .deleteTeam("team/delete/"+teamObj.getId().toString(),token);

        cal.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("Tag Tesponse >>", response.raw().toString());
                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        Toast.makeText(CreateTeamPage.this, response.message(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(CreateTeamPage.this, "Error Deleting ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {

            }
        });
    }

    private void setOldTeamsDeta() {
        etTeamAnem.setText(teamObj.getName());
        etLocation.setText(teamObj.getLocation());
        etAbout.setText(teamObj.getAbout());
        if(teamObj.getWhoCanPost().equalsIgnoreCase("EveryOne"))
        {
            spinpost.setSelection(1);
            whoCanPoststr = whoCanPost[1];
        }
        else{
            spinpost.setSelection(0);
            whoCanPoststr = whoCanPost[0];
        }


        if(teamObj.getWhoCanSee().equalsIgnoreCase("EveryOne"))
        {
            spinsee.setSelection(1);
            whoCanseeStr = WhoCanSee[1];
        }
        else
        {
            spinsee.setSelection(0);
            whoCanseeStr = WhoCanSee[0];
        }

        if(teamObj.getWhoCanMessage().equalsIgnoreCase("EveryOne"))
        {
            spinmess.setSelection(1);
            whoCanMessstr = whoCanMess[1];
        }
        else{
            spinmess.setSelection(0);
            whoCanMessstr = whoCanMess[0];
        }

    }

    private void updateTeam(String teamId) {

        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getTeamsApi().updateTeam("team/edit/"+teamId,token,
                        whoCanPoststr,whoCanseeStr,whoCanMessstr,teamNAme,about);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("Response>>", response.raw().toString());
                if (response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        Toast.makeText(CreateTeamPage.this, "Team Updated ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateTeamPage.this, HomePage.class));
                        finish();

                    }else {
                        Toast.makeText(CreateTeamPage.this, "Error Updating ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {

            }
        });

    }

    private void makeNewTeam(String token) {
        Call<MakeClassResponse> call =  RetrifitClient.getInstance()
                .getTeamsApi().makeNewTeam(token,teamNAme,whoCanPoststr,whoCanseeStr
                ,whoCanMessstr,about);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d(">>Response>>", response.raw().toString());

                if(response!=null)
                {
                    Toast.makeText(CreateTeamPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Log.d(">>Exceptiopn>.", t.toString());
            }
        });

    }

    private void setAdaptersToSpinnsers() {

        ArrayAdapter<String> adapterpost = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, whoCanPost);
        adapterpost.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinpost.setAdapter(adapterpost);

        ArrayAdapter<String> adapterSee = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,WhoCanSee);
        adapterSee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinsee.setAdapter(adapterSee);

        ArrayAdapter<String> adapterMess = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, whoCanMess);
        adapterMess.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinmess.setAdapter(adapterMess);

    }

    private void setUi() {

        spinpost = findViewById(R.id.spinner_who_can_post_to_team);
        spinsee = findViewById(R.id.spinner_who_can_see_post);
        spinmess = findViewById(R.id.spinner_who_can_send_mess);
        createTeamButtion = findViewById(R.id.btn_commit_new_team);
        etTeamAnem = findViewById(R.id.et_new_team_name);
        etLocation = findViewById(R.id.et_new_team_location);
        etAbout = findViewById(R.id.et_about_new_team);
        deleteButton = findViewById(R.id.btn_delete_team);
        toolbar = findViewById(R.id.tb_new_team);
    }
}