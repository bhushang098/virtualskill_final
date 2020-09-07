package com.twilio.video.app.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import static tvi.webrtc.ContextUtils.getApplicationContext;

public class UserRoleAdapter implements AdapterView.OnItemSelectedListener {
    String[] role = { "Student", "Professor"};

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: "+role[position] ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
