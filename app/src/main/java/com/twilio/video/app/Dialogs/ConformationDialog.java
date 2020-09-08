package com.twilio.video.app.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.twilio.video.app.LoginPage;
import com.twilio.video.app.R;

public class ConformationDialog {

    public static int  showConformDialog(Context context,String title,String message
    ,String purpose){



        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        if(purpose.equalsIgnoreCase("log_out"))
                        {
                            SharedPreferences settings = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                            settings.edit().clear().commit();
                            context.startActivity(new Intent(context, LoginPage.class));
                            ((Activity) context).finish();
                        }

                    }
                })


                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.plcicon)
                .show();
        return  0;

    }

}
