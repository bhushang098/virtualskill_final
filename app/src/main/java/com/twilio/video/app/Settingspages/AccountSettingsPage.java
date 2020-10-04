package com.twilio.video.app.Settingspages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.twilio.video.app.MainPages.SettingsActivity;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SettingsResponse.SettingsResponse;
import com.twilio.video.app.SingleUserResponse.Data;
import com.twilio.video.app.SingleUserResponse.SingleUserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSettingsPage extends AppCompatActivity {

    CheckBox graphicDesign, Ai, computerNetwork, pythonPrograming;
    CheckBox  chkGraphic, chkAi, chkcn, chkPython;
    String name, email, location, skill, genderStr, token;
    EditText etName, etMail, etLocation, etSkill;
    TextView tvUpdateAccountSettings;
    List<String> interests = new ArrayList<String>();
    Spinner spinGender;
    String[] gender = {"Male", "Female"};
    ImageView ivback;
    Data userObj = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings_page);
        loadToken();
        setUi();
        getUserByApi();

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //setting Adapters To Spinners
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(adapterGender);


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
                if (chkGraphic.isChecked())
                    interests.add("Graphics Designing ");

                if (chkAi.isChecked())
                    interests.add("AI");


                if (chkcn.isChecked())
                    interests.add("Computer Network");


                if (chkPython.isChecked())
                    interests.add("Programming in Python");


                if (location.isEmpty())
                    etLocation.setError("Fill out This Field");


                if (skill.isEmpty())
                    etSkill.setError("Fill out This Field");


                if (spinGender.getSelectedItemPosition() == 1) {
                    genderStr = "1";
                } else {
                    genderStr = "0";
                }

                if (name.isEmpty() || email.isEmpty() || location.isEmpty() || skill.isEmpty()) {

                } else {
                    //TODO Update Account Settings
                    updateAccountSettings();
                }

            }
        });
    }

    private void loadToken() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token", "");

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
                Toast.makeText(AccountSettingsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserData() {

        etName.setText(userObj.getName());
        etMail.setText(userObj.getEmail());
        etLocation.setText(userObj.getLocation().toString());
        etSkill.setText(userObj.getSkill().toString());

        if(userObj.getGender()==1)
            spinGender.setSelection(0);
        else
            spinGender.setSelection(1);


        if (userObj.getInterests().isEmpty()) {

        } else {
            String[] interestsAry = userObj.getInterests().split("\"");

            for (int i = 1; i < interestsAry.length; i++) {
                if(interestsAry[i].equalsIgnoreCase("Graphics Designing"))
                    chkGraphic.setChecked(true);

                if(interestsAry[i].equalsIgnoreCase("Computer Network"))
                    chkcn.setChecked(true);

                if(interestsAry[i].equalsIgnoreCase("Programming in Python"))
                    chkPython.setChecked(true);

                if(interestsAry[i].equalsIgnoreCase("Ai"))
                    chkAi.setChecked(true);

            }
        }



    }

    private void setUi() {
        graphicDesign = findViewById(R.id.chk_graphic_design);
        Ai = findViewById(R.id.chk_AI);
        computerNetwork = findViewById(R.id.chk_computer_network);
        pythonPrograming = findViewById(R.id.chk_Python_programing);
        spinGender = findViewById(R.id.spinner_gender);

        etName = findViewById(R.id.et_name_user_settings);
        etMail = findViewById(R.id.et_mail_user_settings);
        etLocation = findViewById(R.id.et_location_user_setting);
        etSkill = findViewById(R.id.et_skill_user_Setting);

        tvUpdateAccountSettings = findViewById(R.id.tv_update_account_settings);

        chkGraphic = findViewById(R.id.chk_graphic_design);
        chkAi = findViewById(R.id.chk_AI);
        chkcn = findViewById(R.id.chk_computer_network);
        chkPython = findViewById(R.id.chk_Python_programing);

        ivback = findViewById(R.id.iv_back_edit_info);

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
                        Toast.makeText(AccountSettingsPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(AccountSettingsPage.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

            }
        });

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