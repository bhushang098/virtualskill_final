package com.twilio.video.app.MainPages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SettingsResponse.SettingsResponse;
import com.twilio.video.app.Settingspages.AccountSettingsPage;
import com.twilio.video.app.SingleClassResponse.User;
import com.twilio.video.app.SingleUserResponse.Data;
import com.twilio.video.app.SingleUserResponse.SingleUserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    String[] role = {"Student", "Professor"};
    String[] gender = {"Male", "Female"};
    CheckBox graphicDesign, Ai, computerNetwork, pythonPrograming;
    Spinner spinRole, spinGender;

    String name, email, location, skill, userName, roleStr, genderStr, currentPass, newPass1, newPass2;
    EditText etName, etMail, etLocation, etSkill, etUsername, etCurrentPass, etNewPass1, etnewPass2;
    TextView tvUpdateAccountSettings, tvUpdatePrivateAccnt, tvUpdateUserName,
            tvUpdateProfessor, tvUpdateGender, tvUpdatePass;

    CheckBox chkPrivate,chkGraphic,chkAi,chkcn,chkPython;
    List<String> interests = new ArrayList<String>();

    String token;
    Data userObj = new Data();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        token = getIntent().getStringExtra("token");
        setUi();
        setSpinnerAdalters();
        getUserByApi();

        spinRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleStr = role[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderStr = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvUpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                email = etMail.getText().toString();
                location = etLocation.getText().toString();
                skill = etSkill.getText().toString();
                if(chkGraphic.isChecked())
                    interests.add("Graphics Designing ");

                if(chkAi.isChecked())
                    interests.add("AI");


                if(chkcn.isChecked())
                    interests.add("Computer Network");


                if(chkPython.isChecked())
                    interests.add("Programming in Python");


                if(location.isEmpty())
                    etLocation.setError("Fill out This Field");


                if(skill.isEmpty())
                    etSkill.setError("Fill out This Field");


                if(spinGender.getSelectedItemPosition()==1)
                {
                    genderStr = "1";
                }else {
                    genderStr = "0";
                }

                if(name.isEmpty()||email.isEmpty()||location.isEmpty()||skill.isEmpty())
                {

                }else {
                    //TODO Update Account Settings
                    updateAccountSettings();
                }

            }
        });

        tvUpdatePrivateAccnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkPrivate.isChecked())
                {
                    //TODO Update To private Account
                    makeAccountPrivate();
                }
            }
        });

        tvUpdateUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etUsername.getText().toString();
                updateUserName();
                //TODU Update USer NAme
            }
        });

        tvUpdateProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    updateRole();
                //ToDo Update Role by Passing  roleStr
            }
        });

        tvUpdateGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccountSettings();
            }
        });

        tvUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPass = etCurrentPass.getText().toString();
                newPass1 = etNewPass1.getText().toString();
                newPass2 = etnewPass2.getText().toString();
                if(currentPass.isEmpty()||newPass1.isEmpty()||newPass2.isEmpty())
                {
                    Toast.makeText(SettingsActivity.this, "PassWord Missing", Toast.LENGTH_SHORT).show();
                }else {
                    if(newPass1.equals(newPass2))
                    {
                        // ToDo UpDate New Pass
                        updatePassword();

                    }else {
                        Toast.makeText(SettingsActivity.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void updatePassword() {
        Call<SettingsResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().updatePass(token,currentPass,newPass1,newPass2);

        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                Log.d("Response>>", response.raw().toString());

                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        Toast.makeText(SettingsActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        finish();

                    }else {
                        Toast.makeText(SettingsActivity.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

                Log.d(">>Error>>", t.toString());
            }
        });

    }

    private void updateRole() {

        Call<SettingsResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().applyPro(token);

        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                Log.d("Tag>response >>", response.raw().toString());

                if(response.body()!=null)
                {
                    if (response.body().getStatus())
                    {
                        finish();
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SettingsActivity.this, "Error Updating ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

            }
        });
    }

    private void updateUserName() {
        Call<SettingsResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().updateUserName(token,userName);

        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                Log.d("Response>>>", response.raw().toString());

                if(response.body()!=null)
                {
                    if (response.body().getStatus())
                    {
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(SettingsActivity.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

            }
        });
    }

    private void makeAccountPrivate() {

    }

    private void updateAccountSettings() {

        String i1 = " ",i2 = " ",i3 = " ",i4=" ";
        int lasteleIndex = interests.size()-1;

        if(lasteleIndex>=0)
            i1 = interests.get(0);
        if (lasteleIndex>=1)
            i2 = interests.get(1);
        if (lasteleIndex>=2)
            i3 = interests.get(2);
        if (lasteleIndex>=3)
            i4 = interests.get(3);

        Call<SettingsResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().updateBasicInfo(token,name,email,skill,i1,i2,i3,i4,
                        location,genderStr);

        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                Log.d("Response >>>",response.raw().toString());

                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

                Log.d("Error>>",t.toString());
            }
        });

    }

    private void setSpinnerAdalters() {
        ArrayAdapter<String> adapterRole = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, role);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRole.setAdapter(adapterRole);


        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(adapterGender);
    }

    private void getUserByApi() {

        Call<SingleUserResponse> call = RetrifitClient.getInstance()
                .getUserApi().getUSer(token);
        call.enqueue(new Callback<SingleUserResponse>() {
            @Override
            public void onResponse(Call<SingleUserResponse> call, Response<SingleUserResponse> response) {
                Log.d("User>obJResponse>",response.raw().toString());
                try{
                    if (response.body()==null)
                    {
                        //progressPopup.dismiss();
                        if(response.errorBody()==null){
                            //progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            // showAuthError();
                        }
                    }else {
                        //progressPopup.dismiss();
                        userObj = response.body().getData();
                        setUserData();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Exception>>",e.toString());
                    // progressPopup.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SingleUserResponse> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setUserData() {

        etName.setText(userObj.getName());
        etMail.setText(userObj.getEmail());
        etLocation.setText(userObj.getLocation().toString());
        etSkill.setText(userObj.getSkill().toString());
        if(userObj.getPrivate()==1)
        {
            chkPrivate.setChecked(true);
        }
        etUsername.setText(userObj.getUsername());

        if(userObj.getUserType()==1)
        {
            spinRole.setSelection(1);
        }
        if(userObj.getSex()==1)
            spinGender.setSelection(1);

        if (userObj.getInterests().isEmpty()) {

        } else {
            String[] interestsAry = userObj.getInterests().split("\"");

            for (int i = 1; i < interestsAry.length; i++) {
                if(interestsAry[1].equalsIgnoreCase("Graphics Designing"))
                    chkGraphic.setChecked(true);

                if(interestsAry[1].equalsIgnoreCase("Computer Network"))
                    chkcn.setChecked(true);

                if(interestsAry[1].equalsIgnoreCase("Programming in Python"))
                    chkPython.setChecked(true);

                if(interestsAry[1].equalsIgnoreCase("Ai"))
                    chkAi.setChecked(true);

            }
        }


    }

    private void setUi() {
        spinRole = findViewById(R.id.spinner_role);
        spinGender = findViewById(R.id.spinner_gender);
        graphicDesign = findViewById(R.id.chk_graphic_design);
        Ai = findViewById(R.id.chk_AI);
        computerNetwork = findViewById(R.id.chk_computer_network);
        pythonPrograming = findViewById(R.id.chk_Python_programing);

        etName = findViewById(R.id.et_name_user_settings);
        etMail = findViewById(R.id.et_mail_user_settings);
        etLocation = findViewById(R.id.et_location_user_setting);
        etSkill = findViewById(R.id.et_skill_user_Setting);
        etUsername = findViewById(R.id.et_userName_on_settings);
        etCurrentPass = findViewById(R.id.et_current_pass_on_settings);
        etNewPass1 = findViewById(R.id.et_new_pass1_on_settings);
        etnewPass2 = findViewById(R.id.et_new_pass2_on_settings);

        tvUpdateAccountSettings = findViewById(R.id.tv_update_account_settings);
        tvUpdatePrivateAccnt = findViewById(R.id.tv_update_private_account);
        tvUpdateUserName = findViewById(R.id.tv_update_user_name);
        tvUpdateProfessor = findViewById(R.id.tv_user_role_update);
        tvUpdateGender = findViewById(R.id.tv_user_gender_setting);
        tvUpdatePass = findViewById(R.id.tv_update_pass);

        chkPrivate = findViewById(R.id.chk_private_account);
        chkGraphic = findViewById(R.id.chk_graphic_design);
        chkAi = findViewById(R.id.chk_AI);
        chkcn = findViewById(R.id.chk_computer_network);
        chkPython = findViewById(R.id.chk_Python_programing);

    }


    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        String str = "";
        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.chk_graphic_design:
                str = checked ? "Graphic Design Selected" : "Graphic Design Deselected";
                break;
            case R.id.chk_AI:
                str = checked ? "AngularJS Selected" : "AngularJS Deselected";
                break;
            case R.id.chk_computer_network:
                str = checked ? "chk_computer_network Selected" : "chk_computer_network Deselected";
                break;
            case R.id.chk_Python_programing:
                str = checked ? "chk_Python_programing Selected" : "chk_Python_programing Deselected";
                break;
            case R.id.chk_private_account:
                str = checked ? "chk_Private Account Selected" : "chk_Python_programing Deselected";
                break;
        }
       // Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

}