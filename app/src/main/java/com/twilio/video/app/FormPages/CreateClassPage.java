package com.twilio.video.app.FormPages;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.BodyParamClassClass.MakeClassClass;
import com.twilio.video.app.ClassesModal.Datum;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClassPage extends AppCompatActivity {

    TextView tvClassStartDate,tvClassEdnDate,tvClassStartTime,
    tvJoiningFees;
    EditText etClassName,etlocation,etDuration,etAbout,etjoiningFees;
    RadioGroup radioGroup;
    CheckBox checkBoxFreeClass;
    Button createButton,deleteButton,btnOk;
    private RadioButton radioSetButton;
    Toolbar toolbar;
    Dialog successOnApiDialog;

    DatePickerDialog startdatepicker,enddatePicker;
    String className,location,startDate,endDate,startTime,duration,about;
    String fees;
    UserObj userobj;
    Datum classObj;
    String token;
    int mHour,mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class_page);
        setUi();
        Gson gson = new Gson();
        successOnApiDialog = new Dialog(this);
        userobj = gson.fromJson(getIntent().getStringExtra("userObj"),UserObj.class);
        if(getIntent().getStringExtra("classObj").equalsIgnoreCase("0"))
            classObj = null;
        else
        classObj = gson.fromJson(getIntent().getStringExtra("classObj"),Datum.class);

        token = getIntent().getStringExtra("token");

        if(classObj==null){

        }else {

            toolbar.setTitle("Update "+ classObj.getName());
            etClassName.setText(classObj.getName());
            etlocation.setText(classObj.getLocation());
            startDate = classObj.getStartDate();
            tvClassStartDate.setText("    "+ startDate);

            endDate = classObj.getEndDate();
            tvClassEdnDate.setText("     "+endDate);
            startTime = classObj.getStartTime();
            tvClassStartTime.setText("    "+startTime);
            etDuration.setText(classObj.getDuration().toString());
            createButton.setText("  Update  ");
            className = classObj.getName();
            location = classObj.getLocation();
            duration = classObj.getDuration().toString();
            about = classObj.getAbout();
            fees = classObj.getFee();
            etAbout.setText(classObj.getAbout());
            if(classObj.getFee().equalsIgnoreCase("0"))
            {
                checkBoxFreeClass.setChecked(true);
                etjoiningFees.setText("0");

            }else {
                checkBoxFreeClass.setChecked(false);
                etjoiningFees.setText(classObj.getFee());
            }

            deleteButton.setVisibility(View.VISIBLE);

        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClassByAPI();
            }
        });


        tvClassStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                startdatepicker = new DatePickerDialog(CreateClassPage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                               tvClassStartDate.setText("        "+dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                               startDate = String.valueOf(year)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(dayOfMonth);

                            }
                        }, year, month, day);
                startdatepicker.show();
            }
        });

        tvClassEdnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                enddatePicker = new DatePickerDialog(CreateClassPage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvClassEdnDate.setText("         "+dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                endDate = String.valueOf(year)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(dayOfMonth);
                            }
                        }, year, month, day);
                enddatePicker.show();
            }
        });

        tvClassStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateClassPage.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if(hourOfDay>12)
                                {
                                    tvClassStartTime.setText("      "+(hourOfDay-12)+ ":" + minute+"  PM");
                                }else {
                                    tvClassStartTime.setText("      "+hourOfDay + ":" + minute+"  AM");
                                }


                                startTime = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        checkBoxFreeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxFreeClass.isChecked()){
                    tvJoiningFees.setVisibility(View.GONE);
                    etjoiningFees.setVisibility(View.GONE);
                }else {

                    tvJoiningFees.setVisibility(View.VISIBLE);
                    etjoiningFees.setVisibility(View.VISIBLE);

                }

            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                className = etClassName.getText().toString();
                location = etlocation.getText().toString();
                duration = etDuration.getText().toString();
                about = etAbout.getText().toString();
                fees = etjoiningFees.getText().toString();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                startDate = dateFormat.format(calendar.getTime());
                endDate = dateFormat.format(calendar.getTime());
                startTime = String.valueOf(12)+":"+String.valueOf(45);

                if(className.isEmpty()||location.isEmpty()||startDate.isEmpty()
                ||endDate.isEmpty()||startTime.isEmpty()||duration.isEmpty()||about.isEmpty())
                {
                    Toast.makeText(CreateClassPage.this, "Please Fill All Information", Toast.LENGTH_SHORT).show();
                }else {
                    MakeClassClass newClassObj = new MakeClassClass();

                    newClassObj.setName(className);
                    newClassObj.setLocation(location);
                    newClassObj.setStart_date(Date.valueOf(startDate));
                    newClassObj.setEnd_date(Date.valueOf(endDate));
                    newClassObj.setStart_time(startTime);
                    newClassObj.setDuration(duration);
                    int selectedId=radioGroup.getCheckedRadioButtonId();
                    radioSetButton=(RadioButton)findViewById(selectedId);
                    //Toast.makeText(CreateClassPage.this, radioSetButton.getText(), Toast.LENGTH_SHORT).show();
                    if(radioSetButton.getText().toString().equalsIgnoreCase("Yes"))
                    {
                        newClassObj.setRecurring_class("1");
                    }else
                    {
                        newClassObj.setRecurring_class("0");
                    }
                    newClassObj.setAbout(about);

                    if(checkBoxFreeClass.isChecked()){
                        newClassObj.setFree_class("0");
                        newClassObj.setFee("0");
                    }else {
                        newClassObj.setFree_class("1");
                        newClassObj.setFee(fees);
                    }

                    if(classObj==null)
                    {
                        maknewClassByAPI(newClassObj);

                    }else {
                        updateClassByApi(newClassObj,classObj.getEId());
                    }

                }

            }
        });

    }

    private void updateClassByApi(MakeClassClass newClassObj,int classId) {
        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getClassesApi().updateClassInfo("class/edit/"+String.valueOf(classId),token,
                        newClassObj.getName(),newClassObj.getLocation(),
                        newClassObj.getStart_time(),newClassObj.getStart_date(),
                        newClassObj.getEnd_date(),newClassObj.getRecurring_class(),
                        newClassObj.getDuration(),newClassObj.getAbout(),newClassObj.getFree_class(),
                        newClassObj.getFee(),true);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                if(response.body().getStatus()){
                    showSuccessmessage(className +" Edited Successfully");
                    Toast.makeText(CreateClassPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("Response>>>",response.raw().toString());
                    Toast.makeText(CreateClassPage.this, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Toast.makeText(CreateClassPage.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void maknewClassByAPI(MakeClassClass newClassObj) {
        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getClassesApi().createNewClass(token,
                        newClassObj.getName(),newClassObj.getLocation(),
                        newClassObj.getStart_time(),newClassObj.getStart_date(),
                        newClassObj.getEnd_date(),newClassObj.getRecurring_class(),
                        newClassObj.getDuration(),newClassObj.getAbout(),newClassObj.getFree_class(),
                        newClassObj.getFee(),true);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("Response>>", response.raw().toString());
                if(response.body().getStatus()){
                    showSuccessmessage("Class Created Successfully");
                    Toast.makeText(CreateClassPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("Response>>>",response.raw().toString());
                    Toast.makeText(CreateClassPage.this, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Toast.makeText(CreateClassPage.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteClassByAPI() {
        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getClassesApi().deleteClass("class/delete/"+String.valueOf(classObj.getEId()),token);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("Delete REsponse>>", response.raw().toString());
                if(response.body().getStatus()){
                    Toast.makeText(CreateClassPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    showSuccessmessage(response.body().getMessage());
                }else {
                    Log.d("Response>>>",response.raw().toString());
                    Toast.makeText(CreateClassPage.this, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Toast.makeText(CreateClassPage.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUi() {
        tvClassStartDate = findViewById(R.id.tv_new_class_start_date);
        tvClassStartTime = findViewById(R.id.tv_new_class_start_time);
        tvClassEdnDate = findViewById(R.id.tv_new_class_end_date);
        tvJoiningFees = findViewById(R.id.tv_join_fee_text);
        etClassName = findViewById(R.id.et_new_class_name);
        etlocation = findViewById(R.id.et_new_class_location);
        etDuration = findViewById(R.id.et_new_class_duration);
        checkBoxFreeClass = findViewById(R.id.chbk_free_class);
        etAbout = findViewById(R.id.et_new_class_about);
        createButton = findViewById(R.id.btn_commit_new_class);
        etjoiningFees = findViewById(R.id.et_new_class_joining_fees);
        radioGroup = findViewById(R.id.radio_group_Class);
        toolbar = findViewById(R.id.tb_new_class);
        deleteButton = findViewById(R.id.btn_delete_class);
    }

    public void showSuccessmessage(String message) {
        successOnApiDialog.setContentView(R.layout.class_join_success);
        TextView tvMessage = successOnApiDialog.findViewById(R.id.tv_class_subscribe_message_on_dialog);
        btnOk = successOnApiDialog.findViewById(R.id.btn_ok_class_join);
        successOnApiDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.colorAccent));
        tvMessage.setText(message);
        successOnApiDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successOnApiDialog.dismiss();
                finish();
            }
        });

    }


}